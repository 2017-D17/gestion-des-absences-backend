package dev.gda.api.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.exception.AbsenceException;

@Service
public class AbsenceValidator {

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
	    
	    areDateDebutAndDateFinValid(absence.getDateDebut(), absence.getDateFin());
	    
	    return true;
	    
	  }
	
	  /**
	   * Cette méthode permet de vérifier la validité des dates début et fin de l'absence
	   * 
	   * @param debut
	   *       La date de début
	   * @param fin
	   *       La date de fin
	   * @return
	   * @throws AbsenceException
	   */
	  private boolean areDateDebutAndDateFinValid(LocalDate debut, LocalDate fin) throws AbsenceException {
	    
	    if(fin.isBefore(debut)) {
	      throw new AbsenceException("The end date is before the begin date");
	    }
	    
	    if(LocalDate.now().isAfter(debut)) {
	      throw new AbsenceException("Your begin date is in the past");
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
	private boolean isMotifNotRequired(String motif, AbsenceType type) throws AbsenceException {
		
		if(type != null && type.equals(AbsenceType.CONGE_SANS_SOLDE) && motif == null ) {
			throw new AbsenceException("For this type a motif is required") ;
		}
		return true;
	}

}
