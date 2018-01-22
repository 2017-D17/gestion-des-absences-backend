package dev.gda.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.exception.AbsenceException;
import dev.gda.api.exception.AbsenceNotFoundException;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;

/**
 * Controlleur de la ressource absence
 *
 */
@RestController
@RequestMapping(path = "/absences")
@CrossOrigin
public class AbsenceController {

	@Autowired
	private AbsenceRepository absenceRepository;

	@Autowired
	private CollaborateurRepository collaborateurRepository;

	@Autowired
	private AbsenceValidator absenceValidator;

	/**
	 * Cette méthode permet de renvoyer la liste des demandes d'absence d'un employé
	 * 
	 * Une exception est levée dans le cas où le matricule est null ou vide
	 * 
	 * @param matricule
	 *            le matricule de l'employé
	 * @return La liste des demandes d'absence ou null
	 * 
	 * @throws Exception
	 */
	@GetMapping("/{matricule}")
	public List<Absence> listerAbsenceParCollaborateur(@PathVariable String matricule) throws Exception {

		if (matricule == null || matricule.trim().isEmpty()) {
			throw new Exception("Matricule can be null");
		}

		Collaborateur c = this.collaborateurRepository.findByMatricule(matricule.trim())
				.orElseThrow(() -> new Exception("Employee not found"));

		return this.absenceRepository.findByCollaborateur(c);
	}

	/**
	 * Cette méthode permet de renvoyer la liste des demandes d'absence en fonction
	 * de leur statut
	 * 
	 * Une exception est levée dans le cas où le matricule est null ou vide
	 * 
	 * @param matricule
	 *            le matricule de l'employé
	 * @return La liste des demandes d'absence ou null
	 * 
	 * @throws Exception
	 */
	@GetMapping
	public List<Absence> listerAbsenceParStatut(@RequestParam(value = "statut") Optional<AbsenceStatut> statut)
			throws Exception {

		if (statut.isPresent()) {
			return this.absenceRepository.findByStatut(statut.get());
		}

		return null;
	}

	/**
	 * Cette méthode permet d'ajouter une absence
	 * 
	 * Une exception est levée : si le matricule est null ou vide si l'absence à
	 * ajouter est invalide
	 * 
	 * @param absence
	 *            L'absence a ajouté
	 * 
	 * @throws AbsenceException
	 */
	@PostMapping
	public Absence ajouterAbsence(@RequestBody @Valid Absence absence) throws AbsenceException {

		if (absenceValidator.isValid(absence)) {

			Collaborateur c = this.collaborateurRepository
					.findByMatricule(absence.getCollaborateur().getMatricule().trim())
					.orElseThrow(() -> new AbsenceException("No Employee has been found"));

			absence.setCollaborateur(c);

			if (!this.absenceRepository
					.findInvalidCreneaux(c.getMatricule(), absence.getDateDebut(), absence.getDateFin()).isEmpty()) {
				throw new AbsenceException("A demand has been found");
			}

			absence.setStatut(AbsenceStatut.INITIALE);

			return this.absenceRepository.save(absence);
		}
		return null;
	}

	/**
	 * Cette méthode permet de modifier une absence
	 * 
	 * @param absenceId
	 *            l'id de l'absence
	 * @param absence
	 *            l'absence à modifier
	 * @return
	 * @throws AbsenceException
	 */
	@PutMapping("/{absenceId}")
	public Absence modifierAbsence(@PathVariable Optional<Integer> absenceId, @RequestBody @Valid Absence absence)
			throws AbsenceException {

		if (absenceValidator.isValid(absence)) {
			Absence absenceToModify = absenceId.map(this.absenceRepository::findOne)
					.orElseThrow(() -> new AbsenceNotFoundException("Absence not found"));

			Optional.of(absenceToModify).filter(a -> {
				return a.getStatut().equals(AbsenceStatut.INITIALE) || a.getStatut().equals(AbsenceStatut.REJETEE);
			}).orElseThrow(() -> new AbsenceNotFoundException("Absence can not be modified"));

			Collaborateur c = absenceToModify.getCollaborateur();

			List<Absence> absList = this.absenceRepository.findInvalidCreneaux(c.getMatricule(), absence.getDateDebut(),
					absence.getDateFin());

			if (absList.size() > 1 || absList.get(0).getId() != absenceToModify.getId()) {
				throw new AbsenceException("A demand has been found");
			}

			absenceToModify.setDateDebut(absence.getDateDebut());
			absenceToModify.setDateFin(absence.getDateFin());
			absenceToModify.setMotif(absence.getMotif());
			absenceToModify.setType(absence.getType());
			absenceToModify.setStatut(AbsenceStatut.INITIALE);

			return this.absenceRepository.save(absenceToModify);
		}

		return null;
	}

}
