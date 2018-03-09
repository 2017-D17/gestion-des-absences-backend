package dev.gda.api.controller;

import static dev.gda.api.util.ModelViewUtils.absence2AbsenceView;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.exception.AbsenceException;
import dev.gda.api.exception.AbsenceNotFoundException;
import dev.gda.api.exception.CollaborateurNotFoundException;
import dev.gda.api.modelview.AbsenceView;
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
	 * @param matricule
	 *            le matricule de l'employé
	 * @return La liste des demandes d'absence ou null
	 * 
	 * @throws AbsenceException
	 * @throws CollaborateurNotFoundException 
	 */
	@GetMapping("/{matricule}")
	@Secured("ROLE_USER")
	public List<AbsenceView> listerAbsenceParCollaborateur(@PathVariable Optional<String> matricule) throws AbsenceException, CollaborateurNotFoundException {


	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Collaborateur c = this.collaborateurRepository.findByEmail(auth.getName())
				.orElseThrow(() -> new CollaborateurNotFoundException("Collaborateur not found"));
			
		return Optional.of(c)
				.map(this.absenceRepository::findByCollaborateur)
				.map(abs ->  abs.stream().map(absence2AbsenceView).collect(toList()))
				.orElseGet(() -> new ArrayList<AbsenceView>());
	}

	/**
	 * Cette méthode permet de renvoyer la liste de toutes demandes d'absence en fonction
	 * de leur statut
	 * 
	 * 
	 * @param statut le statut de l'absence
	 * @return La liste des demandes d'absence ou null
	 * @throws AbsenceException 
	 * @throws CollaborateurNotFoundException 
	 * 
	 */
	@GetMapping
	@Secured("ROLE_MANAGER")
	public List<AbsenceView> listerAbsence(@RequestParam(value = "statut", required = false) Optional<AbsenceStatut> statut) throws AbsenceException, CollaborateurNotFoundException {

		List<AbsenceView> absences = this.absenceRepository.findAll().stream().map(absence2AbsenceView).collect(toList());
		
		if (statut.isPresent()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    
			Collaborateur manager = this.collaborateurRepository.findByEmail(auth.getName())
					.orElseThrow(() -> new CollaborateurNotFoundException("Collaborateur not found"));

			if (!manager.getSubalternes().isEmpty()) {
				List<AbsenceView> absencesSub = new ArrayList<>();
				manager.getSubalternes().forEach(sub -> {
					List<AbsenceView> abs = getAllSubalterneAbsencesByStatut(sub, absences, statut.get());
					if (!abs.isEmpty()) {
						absencesSub.addAll(abs);
					}
				});
				return absencesSub;
			} 
		}

		return absences;
	}
	
	/**
	 * Cette méthode permet de récupérer la liste des demandes d'un subalterne en fonction de son statut
	 * 
	 * @param subalterne le subalterne
	 * @param absences la liste des demandes d'absence de tous les collaborateurs 
	 * @param statut le statut de la demande
	 *       
	 * @return la liste des demandes d'absence du subalterne ou une liste vide
	 */
	private List<AbsenceView> getAllSubalterneAbsencesByStatut(Collaborateur subalterne, List<AbsenceView> absences, AbsenceStatut statut){
	  return absences.stream()
  	  .filter(a -> a.getStatut().equals(statut) && a.getCollaborateur().getMatricule().equals(subalterne.getMatricule()))
  	  .collect(toList());
	}
	

	/**
	 * Cette méthode permet d'ajouter une absence
	 * 
	 * @param absence l'absence a ajouté
	 * 
	 * @throws AbsenceException
	 * @throws CollaborateurNotFoundException 
	 */
	@PostMapping
	@Secured("ROLE_USER")
	@ResponseStatus(HttpStatus.CREATED)
	public AbsenceView ajouterAbsence(@RequestBody @Valid Absence absence) throws AbsenceException, CollaborateurNotFoundException {

		if (absenceValidator.isValid(absence)) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			Collaborateur c = this.collaborateurRepository.findByEmail(auth.getName())
					.orElseThrow(() -> new CollaborateurNotFoundException("Collaborateur not found"));

			absence.setCollaborateur(c);

			if (!this.absenceRepository
					.findInvalidCreneaux(c.getMatricule(), absence.getDateDebut(), absence.getDateFin()).isEmpty()) {
				throw new AbsenceException("A demand has been found");
			}

			absence.setStatut(AbsenceStatut.INITIALE);

			return absence2AbsenceView.apply(this.absenceRepository.save(absence));
		}
		return null;
	}

	/**
	 * Cette méthode permet de modifier une absence
	 * 
	 * @param absenceId l'id de l'absence
	 * @param absence l'absence à modifier
	 * @return l'absence modifiée
	 * @throws AbsenceException
	 */
	@PutMapping("/{absenceId}")
	@Secured("ROLE_USER")
	public AbsenceView modifierAbsence(@PathVariable Optional<Integer> absenceId, @RequestBody @Valid Absence absence)
			throws AbsenceException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (absenceValidator.isValid(absence)) {
			Absence absenceToModify = absenceId.map(this.absenceRepository::findOne)
					.orElseThrow(() -> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));

			Optional.of(absenceToModify).filter(a -> {
				return a.getStatut().equals(AbsenceStatut.INITIALE) || a.getStatut().equals(AbsenceStatut.REJETEE) || absenceToModify.getType().equals(AbsenceType.RTT_EMPLOYEUR);
			}).orElseThrow(() -> new AbsenceNotFoundException("Absence can not be modified"));
			
			Collaborateur c = absenceToModify.getCollaborateur();
			
			if(!auth.getName().equalsIgnoreCase(c.getEmail())) {
				throw new AbsenceException("");
			}
			
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

			return absence2AbsenceView.apply(this.absenceRepository.save(absenceToModify));
		}

		return null;
	}


	/**
	 * Cette méthode permet de modifier le statut 
	 * d'une demande  d'absence
	 * 
	 * Une exception est levée dans le cas où l'id est null ou vide
	 * 
	 * @param absence l'absence a ajouté
	 * @throws CollaborateurNotFoundException 
	 * @throws AbsenceException
	 */
	@PatchMapping("/{absenceId}")
	@Secured("ROLE_MANAGER")
	public AbsenceView modifierStatutAbsence(@PathVariable Optional<Integer> absenceId, @RequestBody Absence absence) throws AbsenceException, CollaborateurNotFoundException {

		checkAbsenceForModifierStatut(absence);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collaborateur manager = this.collaborateurRepository.findByEmail(auth.getName())
		  .orElseThrow(() -> new CollaborateurNotFoundException("Collaborateur not found"));

		Absence absenceFromRepo = absenceId.map(this.absenceRepository::findOne).orElseThrow(() -> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));
		
		if(!manager.getSubalternes().contains(absenceFromRepo.getCollaborateur())) {
		  throw new AbsenceException("Can not update this absence");
		}
		
		absenceFromRepo.setStatut(absence.getStatut());
		return absence2AbsenceView.apply(this.absenceRepository.save(absenceFromRepo));
		

	}
	
	/**
	 * Cette méthode permet de valider 
	 * l'absence lors de la mise à jour du statut
	 * 
	 * @param absence l'absence à valider
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
	 * @param absenceId l'identifiant de l'absence à supprimer
	 *
	 * @throws AbsenceException 
	 */
	@DeleteMapping("/{absenceId}")
	@Secured("ROLE_USER")
	public void supprimerAbsence(@PathVariable Optional<Integer> absenceId) throws AbsenceException{

	  Absence abs = absenceId.map(this.absenceRepository::findOne)
	              .orElseThrow(()-> new AbsenceNotFoundException(ABSENCE_NOT_FOUND));
	  
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  if(!abs.getCollaborateur().getEmail().equals(auth.getName())) {
	    throw new AbsenceException("Can not delete this absence");
	  }
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
