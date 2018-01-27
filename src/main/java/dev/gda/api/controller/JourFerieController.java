package dev.gda.api.controller;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.JourFerie;
import dev.gda.api.entite.JourFerieType;
import dev.gda.api.exception.JourFerieException;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;
import dev.gda.api.repository.JourFerieRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import dev.gda.api.exception.JourFerieNotFoundException;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur de la ressource jour férié
 */
@RestController
@RequestMapping(path = "/jours_feries")
@CrossOrigin
public class JourFerieController {

	@Autowired
	private JourFerieRepository jourFerieRepository;

	@Autowired
	private CollaborateurRepository collaborateurRepository;

	@Autowired
	private AbsenceRepository absenceRepository;

	/**
	 * Cette mérhode permet de lister tous les jours fériés
	 * 
	 * @return Une liste de tous les jours fériés si elle existe une liste vide sinon
	 */
	@GetMapping
	public List<JourFerie> listerJoursFeries() {
		return this.jourFerieRepository.findAll();
	}

	/**
	 * Cette mérhode permet d'ajouter un jour férié
	 * 
	 * @return le jour férié sauvegardé
	 * @throws JourFerieException
	 *
	 */
	@PostMapping
	@Secured("ROLE_ADMIN")
	@ResponseStatus(HttpStatus.CREATED)
	public JourFerie ajouterJourFerie(@RequestBody @Valid JourFerie jourFerie)
			throws JourFerieException {
		checkIfJourFerieValid(jourFerie);
		// il est interdit de saisir un jour férié à la même date qu'un autre jour férié
		if (!this.jourFerieRepository.findByDate(jourFerie.getDate()).isEmpty()) {
			throw new JourFerieException("Day off already exist for this date");
		}

		if (jourFerie.getType().equals(JourFerieType.RTT_EMPLOYEUR)) {

			// * Si une RTT employeur est créée alors le système créé une demande d'abence
			// au statut INITIALE.
			this.collaborateurRepository.findAll().stream().forEach(c -> {
				Absence a = new Absence();
				a.setDateDebut(jourFerie.getDate());
				a.setDateFin(jourFerie.getDate());
				a.setType(AbsenceType.RTT_EMPLOYEUR);
				a.setStatut(AbsenceStatut.INITIALE);
				a.setCollaborateur(c);
				this.absenceRepository.save(a);
			});
		}
		return this.jourFerieRepository.save(jourFerie);
	}

	/**
	 * Cette méthode permet de modifier un jour férié
	 * 
	 * @param jourFerieId l'id du jour férié
	 * @param jourFerie le jour férié
	 * @return le jour férié modifié
	 * 
	 * @throws JourFerieException
	 */
	@PutMapping("/{jourFerieId}")
	@Secured("ROLE_ADMIN")
	public JourFerie modifierJourFerie(@PathVariable Optional<Integer> jourFerieId,
			@RequestBody @Valid JourFerie jourFerie) throws JourFerieException {

		checkIfJourFerieValid(jourFerie);
		// il est interdit de saisir un jour férié à la même date qu'un autre jour férié
		List<JourFerie> jfs = this.jourFerieRepository.findByDate(jourFerie.getDate());
		if (!jfs.isEmpty() && jfs.size() > 1) {
			throw new JourFerieException("Day off already exist for this date");
		}

		JourFerie jourFerieToModify = jourFerieId.map(this.jourFerieRepository::findOne)
				.orElseThrow(() -> new JourFerieNotFoundException("Day off not found"));

		// Il n'est pas possible de modifier une RTT employeur VALIDEE
		if (jourFerieToModify.getType().equals(JourFerieType.RTT_EMPLOYEUR)) {
			gestionJourFerieTypeRttEmployeurInModidierAbsenceMethod(jourFerieToModify, jourFerie);
		}

		jourFerieToModify.setDate(jourFerie.getDate());
		jourFerieToModify.setType(jourFerie.getType());
		jourFerieToModify.setCommentaire(jourFerie.getCommentaire());
		return this.jourFerieRepository.save(jourFerieToModify);
	}
	
	
	/**
	 * Cette méthode permet de gérer les jours jours de type Rtt employeur dans le cas d'une modification
	 *  
	 * @param jourFerieToModify
	 * @param jourFerie
	 * @throws JourFerieException
	 */
	private void gestionJourFerieTypeRttEmployeurInModidierAbsenceMethod(JourFerie jourFerieToModify, JourFerie jourFerie) throws JourFerieException {
		
		List<Absence> absences = this.absenceRepository.findByTypeAndDateDebut(AbsenceType.RTT_EMPLOYEUR,
				jourFerieToModify.getDate());
		if (!absences.isEmpty()) {

			if (absences.get(0).getStatut().equals(AbsenceStatut.VALIDEE)) {
				throw new JourFerieException("Day off can not be modified");
			} 

			if (jourFerie.getType().equals(JourFerieType.RTT_EMPLOYEUR)
					&& !jourFerie.getDate().isEqual(jourFerieToModify.getDate())) {
				absences.stream().forEach(a -> {
					a.setDateDebut(jourFerie.getDate());
					a.setDateFin(jourFerie.getDate());
					this.absenceRepository.save(a);
				});
			}else {
				absences.stream().forEach(a -> {
					this.absenceRepository.delete(a.getId());
				});
			}
		}
	}

	/**
	 * Cette méthode permet de verifier la validité du jour férié
	 * 
	 * @param jourFerie le jour férié à valider
	 * 
	 * @throws JourFerieException
	 */
	private void checkIfJourFerieValid(JourFerie jourFerie) throws JourFerieException {

		// un jour férié ne peut pas être saisi dans le passé
		if (jourFerie.getDate().isBefore(LocalDate.now())) {
			throw new JourFerieException("Date is in the past");
		}

		// * le commentaire est obligatoire pour les jours feriés.
		if (jourFerie.getType().equals(JourFerieType.JOUR_FERIE)) {

			if (jourFerie.getCommentaire() == null || jourFerie.getCommentaire().trim().isEmpty()) {
				throw new JourFerieException("A comment is required for this type");
			}
		} else {
			if (jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY)
					|| jourFerie.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				throw new JourFerieException("Date is invalid for this type");
			}
		}

	}

	/**
	 * Cette méthode permet de supprimer un jour férié
	 * 
	 * 
	 * @param jourFerieId l'id du jour férié
	 *            
	 * @throws JourFerieException
	 */
	@DeleteMapping("/{jourFerieId}")
	@Secured("ROLE_ADMIN")
	public void supprimerJourFerie(@PathVariable Optional<Integer> jourFerieId)
			throws JourFerieException {
		JourFerie jf = jourFerieId.map(this.jourFerieRepository::findOne)
				.orElseThrow(() -> new JourFerieNotFoundException("Day off not Found"));

		if (jf.getDate().isBefore(LocalDate.now())) {
			throw new JourFerieException("Day off is already past");
		}
		//il n'est pas possible de supprimer une RTT employeur validée
		if (jf.getType().equals(JourFerieType.RTT_EMPLOYEUR)) {
			
			List<Absence> absences = this.absenceRepository.findByTypeAndDateDebut(AbsenceType.RTT_EMPLOYEUR,
					jf.getDate());
			if (!absences.isEmpty()) {
				if (absences.get(0).getStatut().equals(AbsenceStatut.VALIDEE)) {
					throw new JourFerieException("Day off can not be deleted");
				}else {
					absences.stream().forEach(a -> this.absenceRepository.delete(a.getId()));
				}
			}
		}
		this.jourFerieRepository.delete(jf.getId());
	}

}
