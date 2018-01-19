package dev.gda.api.controller;

import java.time.LocalDate;
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
import dev.gda.api.entite.Type;
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
	 * @throws Exception
	 */
	@GetMapping("/{matricule}")
	public List<Absence> listerAbsence(@PathVariable String matricule,
			@RequestParam(value = "statut", required = false) Optional<Statut> statut) throws Exception {

		if (matricule == null || matricule.trim().isEmpty()) {
			throw new Exception("Matricule can be null");
		}

		Collaborateur c = this.collaborateurRepository.findByMatricule(matricule.trim())
				.orElseThrow(() -> new Exception("Employee not found"));

		if (statut.isPresent()) {
			return this.absenceRepository.findByCollaborateurAndStatut(c, statut.get());
		}

		return this.absenceRepository.findByCollaborateur(c);
	}

	/**
	 * Cette méthode permet d'ajouter une absence
	 * 
	 * Une exception est levée dans le cas où le matricule est null ou vide
	 * 
	 * @param absence
	 * 			L'absence a ajouté
	 * @throws Exception
	 */
	@PostMapping
	public Absence ajouterAbsence(@RequestBody Absence absence) throws Exception {

		AbsenceValidator absenceValidator = new AbsenceValidator();

		if (absenceValidator.isValid(absence)) {

			Collaborateur c = this.collaborateurRepository
					.findByMatricule(absence.getCollaborateur().getMatricule().trim())
					.orElseThrow(() -> new Exception("No Employee has been found"));

			absence.setCollaborateur(c);

			// * une fois créée, ma demande est au statut INITIALE
			absence.setStatut(Statut.INITIALE);
			// * une demande d'absence ne modifie pas le solde des compteurs de congés.
			// Cette opération est effectuée par le traitement de nuit.

			return this.absenceRepository.save(absence);
		}
		return null;
	}
}
