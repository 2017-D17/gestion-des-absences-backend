package dev.gda.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.exception.AbsenceException;
import dev.gda.api.exception.AbsenceNotFoundException;
import dev.gda.api.repository.AbsenceRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

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
	
	/**
	 * Cette méthode permet de modifier une absence
	 * 
	 * @param absenceId
	 *     l'id de l'absence
	 * @param absence
	 *     l'absence à modifier
	 * @return
	 * @throws AbsenceException
	 */
	@PutMapping("/{absenceId}")
	public Absence modifierAbsence(@PathVariable Optional<Integer> absenceId, @RequestBody @Valid Absence absence) throws AbsenceException {

	  if (absenceValidator.isValid(absence)) {
	    Absence absenceToModify = absenceId.map(this.absenceRepository::findOne)
	          .orElseThrow(() -> new AbsenceNotFoundException("Absence not found"));
	      
      Optional.of(absenceToModify)
      .filter(a -> {return a.getStatut().equals(AbsenceStatut.INITIALE) || a.getStatut().equals(AbsenceStatut.REJETEE);})
      .orElseThrow(() -> new AbsenceNotFoundException("Absence can not be modified"));

      Collaborateur c = absenceToModify.getCollaborateur();
      
      List<Absence> absList = this.absenceRepository.findInvalidCreneaux(c.getMatricule(), absence.getDateDebut(), absence.getDateFin());
      
      if(absList.size() > 1 || absList.get(0).getId() != absenceToModify.getId()) {
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
