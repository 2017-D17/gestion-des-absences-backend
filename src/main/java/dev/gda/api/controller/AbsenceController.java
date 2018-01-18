package dev.gda.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
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
	
	@GetMapping("/{matricule}")
	public List<Absence> listerAbsence(@PathVariable String matricule) throws Exception{
		
		if(matricule == null || matricule.trim().isEmpty()) {
			throw new Exception("Matricule can be null");
		}
		
		Collaborateur c = this.collaborateurRepository.findByMatricule(matricule.trim()).orElseThrow(() -> new Exception("Employee not found") );
		return this.absenceRepository.findByCollaborateur(c);
	}
}
