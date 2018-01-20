package dev.gda.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Type;
import dev.gda.api.exception.AbsenceException;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class AbsenceValidator {
	
	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;
	
	/**
	 * Cette méthode teste si l'absence est valide ou non
	 * 
	 * @param absence 
	 *       L'absence a validé
	 * @return
	 *       true si l'absence est valide
	 *       
	 * @throws AbsenceException
	 */
	public Boolean isValid(Absence absence) throws AbsenceException {
		
		if(absence == null) {
			throw new AbsenceException("There is no absence to save");
		}
		
		isMotifNotRequired(absence.getMotif(), absence.getType());
		
		if(absence.getCollaborateur() == null || absence.getCollaborateur().getMatricule() == null || absence.getCollaborateur().getMatricule().trim().isEmpty()) {
			throw new AbsenceException("No Employee has been found");
		}
		
		areDateDebutDateFinValid(absence.getCollaborateur().getMatricule().trim(), absence.getDateDebut(), absence.getDateFin());
		
		
		this.collaborateurRepository.findByMatricule(absence.getCollaborateur().getMatricule().trim()).orElseThrow(() -> new AbsenceException("No Employee has been found"));
		
		return true;
		
	}
	
	/**
	 * Cette méthode permet de vérifier la validité des dates début et fin de l'absence
	 * 
	 * @param matricule
	 *       Le matricule du demandeur
	 * @param debut
	 *       La date de début
	 * @param fin
	 *       La date de fin
	 * @return
	 * @throws AbsenceException
	 */
	private boolean areDateDebutDateFinValid(String matricule, LocalDate debut, LocalDate fin) throws AbsenceException {
		
		if(debut == null || fin == null) {
			throw new AbsenceException("There is no absence to save");
		}

		if(LocalDate.now().isAfter(debut)) {
			throw new AbsenceException("Your begin date is in the past");
		}

		if(fin.isBefore(debut)) {
			throw new AbsenceException("The end date is before the begin date");
		}
		
		if(!this.absenceRepository.findInvalidCreneaux(matricule, debut, fin).isEmpty()) {
			throw new AbsenceException("A demand has been found");
		}
		
		return true;
	}
	
	/**
	 * Cette méthode permet de vérifier si le motif de l'absence est requise
	 * 
	 * Sachant qu'un motif n'est obligatoire que si le type de demande est congés sans solde
	 * 
	 * @param motif
	 *       Le motif de l'absence
	 * @param type
	 *       Le type de l'absence
	 *       
	 * @return
	 *       true si elle n'est pas requise
	 *       
	 * @throws AbsenceException
	 */
	private boolean isMotifNotRequired(String motif, Type type) throws AbsenceException {
		
		if(type != null) {
			
			if(type.equals(Type.CONGE_SANS_SOLDE) && motif == null ) {
				throw new AbsenceException("For this type a motif is required") ;
			}
		}
		return true;
	}

}
