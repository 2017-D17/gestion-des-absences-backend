package dev.gda.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
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

	private final String ABSENCE_NOT_FOUND = "Absence not found";

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
	 * @throws AbsenceException
	 */
	@GetMapping("/{matricule}")
	public List<Absence> listerAbsenceParCollaborateur(@PathVariable String matricule) throws AbsenceException {

		if (matricule == null || matricule.trim().isEmpty()) {
			throw new AbsenceException("Matricule can be null");
		}

		Collaborateur c = this.collaborateurRepository.findByMatricule(matricule.trim())
				.orElseThrow(() -> new AbsenceException("Employee not found"));

		return this.absenceRepository.findByCollaborateur(c);
	}

	/**
	 * Cette méthode permet de renvoyer la liste des demandes d'absence en fonction
	 * de leur statut
	 * 
	 * 
	 * @param statut
	 *            le statut de l'absence
	 * @return La liste des demandes d'absence ou null
	 * 
	 */
	@GetMapping
	public List<Absence> listerAbsenceParStatut(@RequestParam(value = "statut") Optional<AbsenceStatut> statut) {

		if (statut.isPresent()) {
			return this.absenceRepository.findByStatut(statut.get());
		}

		return new ArrayList<>();
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
					.orElseThrow(() -> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));

			Optional.of(absenceToModify).filter(a -> {
				return a.getStatut().equals(AbsenceStatut.INITIALE) || a.getStatut().equals(AbsenceStatut.REJETEE);
			}).orElseThrow(() -> new AbsenceNotFoundException("Absence can not be modified"));

			if(absenceToModify.getType().equals(AbsenceType.RTT_EMPLOYEUR)) {
				  throw new AbsenceException("Can not modify this type of absence");
			 }
			
			Collaborateur c = absenceToModify.getCollaborateur();

			List<Absence> absList = this.absenceRepository.findInvalidCreneaux(c.getMatricule(), absence.getDateDebut(),
					absence.getDateFin());

			if (!absList.isEmpty() && (absList.size() > 1 || absList.get(0).getId() != absenceToModify.getId())) {
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


	/**
	 * Cette méthode permet de modifier le statut 
	 * d'une demande  d'absence
	 * 
	 * Une exception est levée dans le cas où l'id est null ou vide
	 * 
	 * @param absence
	 * 			L'absence a ajouté
	 * @throws Exception
	 */
	@PatchMapping("/{absenceId}")
	public Absence modifierStatutAbsence(@PathVariable Optional<Integer> absenceId, @RequestBody Absence absence) throws AbsenceException {
		checkAbsenceForModifierStatut(absence);

		Absence absenceFromRepo = absenceId.map(this.absenceRepository::findOne).orElseThrow(() -> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));
		absenceFromRepo.setStatut(absence.getStatut());
		return this.absenceRepository.save(absenceFromRepo);

	}
	
	/**
	 * Cette méthode permet de valider 
	 * l'absence lors de la mise à jour du statut
	 * 
	 * @param absence
	 * 			l'absence à valider
	 * 
	 * @throws AbsenceException
	 */
	private void checkAbsenceForModifierStatut(Absence absence) throws AbsenceException {

		if(absence.getStatut() == null) {
			throw new AbsenceException("Statut is missing");
		}
		
		if(absence.getStatut().equals(AbsenceStatut.EN_ATTENTE_VALIDATION) || absence.getStatut().equals(AbsenceStatut.INITIALE)) {
			throw new AbsenceException("Can not pass in this status");
		}
	}


	/**
	 * Cette méthode permet de supprimer une absence
	 * 
	 * @param absenceId
	 *       L'identifiant de l'absence à supprimer
	 *
	 * @throws AbsenceException 
	 */
	@DeleteMapping("/{absenceId}")
	public void supprimerAbsence(@PathVariable Optional<Integer> absenceId) throws AbsenceException{
	  
	  Absence abs = absenceId.map(this.absenceRepository::findOne)
	              .orElseThrow(()-> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));
	  
	  LocalDate now = LocalDate.now();
	  
	  if(abs.getDateDebut().isBefore(now) || abs.getDateDebut().isEqual(now) ) {
	    throw new AbsenceException("Absence is already begin");
	  }
	  
	  if(abs.getType().equals(AbsenceType.RTT_EMPLOYEUR)) {
		  throw new AbsenceException("Can not delete this type of absence");
	  }
		  
	  
	  this.absenceRepository.delete(absenceId.get());
	}
}
