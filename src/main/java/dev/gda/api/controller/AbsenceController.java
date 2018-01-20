package dev.gda.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.Statut;
import dev.gda.api.exception.AbsenceException;
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
	private AbsenceValidator absenceValidator;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;

	/**
	 * Cette méthode permet de renvoyer la liste des demandes d'absence d'un employé
	 * suivant le statut s'il est valorisé
	 * 
	 * Une exception est levée dans le cas où le matricule est null ou vide
	 * 
	 * @param matricule
	 *            le matricule de l'employé
	 * @return 
	 * 			  La liste des demandes d'absence ou null
	 * 
	 * @throws AbsenceException
	 */
	@GetMapping("/{matricule}")
	public List<Absence> listerAbsence(@PathVariable String matricule,
			@RequestParam(value = "statut", required = false) Optional<Statut> statut) throws AbsenceException {

		if (matricule == null || matricule.trim().isEmpty()) {
			throw new AbsenceException("Matricule can be null");
		}

		Collaborateur c = this.collaborateurRepository.findByMatricule(matricule.trim())
				.orElseThrow(() -> new AbsenceException("Employee not found"));

		if (statut.isPresent()) {
			return this.absenceRepository.findByCollaborateurAndStatut(c, statut.get());
		}

		return this.absenceRepository.findByCollaborateur(c);
	}

	/**
	 * Cette méthode permet d'ajouter une absence
	 * 
	 * Une exception est levée :
	 *   si le matricule est null ou vide
	 *   si l'absence à ajouter est invalide
	 * 
	 * @param absence
	 * 			L'absence a ajouté
	 * 
	 * @throws AbsenceException
	 */
	@PostMapping
	public Absence ajouterAbsence(@RequestBody Absence absence) throws AbsenceException {

		if (absenceValidator.isValid(absence)) {

			Collaborateur c = this.collaborateurRepository
					.findByMatricule(absence.getCollaborateur().getMatricule().trim())					
					.orElseThrow(() -> new AbsenceException("No Employee has been found"));

			absence.setCollaborateur(c);

			absence.setStatut(Statut.INITIALE);

			return this.absenceRepository.save(absence);
		}
		return null;
	}
}
