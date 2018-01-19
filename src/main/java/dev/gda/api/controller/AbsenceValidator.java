package dev.gda.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Type;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class AbsenceValidator {
	
	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;
	
	public Boolean isValid(Absence absence) throws Exception {
		
		if(absence == null) {
			throw new Exception("There is no absence to save");
		}
		
		isMotifRequired(absence.getMotif(), absence.getType());
		
		DateDebutDateFinAreValid(absence.getDateDebut(), absence.getDateFin());
				
		
		if(absence.getCollaborateur() == null || absence.getCollaborateur().getMatricule() == null || absence.getCollaborateur().getMatricule().trim().isEmpty()) {
			throw new Exception("No Employee has been found");
		}
		
		this.collaborateurRepository.findByMatricule(absence.getCollaborateur().getMatricule().trim()).orElseThrow(() -> new Exception("No Employee has been found"));
		
		return true;
		
	}
	
	private boolean DateDebutDateFinAreValid(LocalDate debut, LocalDate fin) throws Exception {
		
		if(debut == null || fin == null) {
			throw new Exception("There is no absence to save");
		}
		
//		une demande d'absence débute au plus tôt à partir de J+1
		if(LocalDate.now().isAfter(debut)) {
			throw new Exception("Your begin date is in the past");
		}
		
//		* la date de fin est supérieure ou égale à la date de début
		if(fin.isBefore(debut)) {
			throw new Exception("The end date is before the begin date");
		}
		
//		* il est interdit de faire une demande qui chevauche une demande existante, sauf si cette dernière est rejetée.
		// st >= start && df <= fin
		if(this.absenceRepository.findByDateDebutAndDateFin(debut, fin) == null) {
			throw new Exception("A demand has been found");
		}
		
		return true;
	}
	
	
	private boolean isMotifRequired(String motif, Type type) throws Exception {
		
//		* le motif n'est obligatoire que si le type de demande est congés sans solde
		if(type != null) {
			
			if(type.equals(Type.CONGE_SANS_SOLDE) && motif == null ) {
				throw new Exception("For this type a motif is required") ;
			}
		}
		return true;
	}

}
