package dev.gda.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.CollaborateurView;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;
import dev.gda.api.util.ModelViewUtils;

@Service
public class InitialiserDonneesServiceDev implements InitialiserDonneesService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	//@PersistenceContext
	//EntityManager em;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;

	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Value("${gda.server.users.url}")
	private String server;
		
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;
	
	public InitialiserDonneesServiceDev() {
		this.rest = new RestTemplate();
		this.headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "*/*");
	}

	public CollaborateurView[] getCollaborateursFromServeur(String uri) {
	    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
	    
	    ResponseEntity<CollaborateurView[]> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity,CollaborateurView[].class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	  }
	
	@Override
	@Transactional
	public void initialiser() {
		
		LOG.info("Retrieve the employees from "+ server);
		
		List<CollaborateurView> utilisateurs = Arrays.asList(getCollaborateursFromServeur("/collaborateurs"));	
		utilisateurs.stream()
			.map( ModelViewUtils::CollaborateurViewToCollaborateur)
			.forEach(collaborateurRepository::save);
		
		List<Collaborateur> collabsFromDb = collaborateurRepository.findAll();
		
		utilisateurs.stream().forEach(u -> {
			Collaborateur col;
			if(!u.getSubalternes().isEmpty()) {
				col = collabsFromDb.stream()
						.filter(c -> c.getMatricule().equals(u.getMatricule()))
						.findFirst().get();

				col.setSubalternes(getAllCollaborateurSubalternes(u.getSubalternes(), collabsFromDb));
			} else {
				
			}
		});
				
		Collaborateur c = collabsFromDb.get(2);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c);
		addAbsence(LocalDate.of(2018, 01, 28),LocalDate.of(2018, 01, 28), AbsenceStatut.INITIALE, c);
		c.setConges(c.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c);
		//em.persist(c);
		collaborateurRepository.save(c);

		
		setDefaultAdmin(collabsFromDb);
		
		Collaborateur c1 = collabsFromDb.get(3);
		addAbsence(LocalDate.of(2018, 01, 30),LocalDate.of(2018, 01, 30), AbsenceStatut.INITIALE, c1);
		addAbsence(LocalDate.of(2018, 02, 05),LocalDate.of(2018, 02, 25), AbsenceStatut.INITIALE, c1);
		c1.setConges(c1.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 28),LocalDate.of(2018, 03, 28), AbsenceStatut.EN_ATTENTE_VALIDATION, c1);
		
		
		addAbsence(LocalDate.of(2018, 01, 30),LocalDate.of(2018, 01, 30), AbsenceStatut.INITIALE, c1);
		addAbsence(LocalDate.of(2018, 02, 05),LocalDate.of(2018, 02, 25), AbsenceStatut.INITIALE, c1);
		c1.setConges(c1.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 28),LocalDate.of(2018, 03, 28), AbsenceStatut.EN_ATTENTE_VALIDATION, c1);
		
		collaborateurRepository.save(c1);
		
		Collaborateur c2 = collabsFromDb.get(3);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c2);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c2);
		c2.setConges(c2.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c2);
		
		collaborateurRepository.save(c2);
		
		
		Collaborateur c3 = collabsFromDb.get(4);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c3);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c3);
		c3.setConges(c3.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c3);
		
		collaborateurRepository.save(c3);
		
		Collaborateur c4 = collabsFromDb.get(5);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c4);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c4);
		c4.setConges(c4.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c4);
		
		collaborateurRepository.save(c4);
		
		Collaborateur c5 = collabsFromDb.get(6);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c5);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c5);
		c5.setConges(c5.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c5);
		
		collaborateurRepository.save(c5);
		
		Collaborateur c6 = collabsFromDb.get(7);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c6);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c6);
		c6.setConges(c6.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c6);
		
		collaborateurRepository.save(c6);
		
		Collaborateur c7 = collabsFromDb.get(8);
		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c7);
		addAbsence(LocalDate.of(2018, 01, 29),LocalDate.of(2018, 01, 29), AbsenceStatut.INITIALE, c7);
		c7.setConges(c7.getConges() - 1);
		addAbsence(LocalDate.of(2018, 02, 28),LocalDate.of(2018, 03, 06), AbsenceStatut.EN_ATTENTE_VALIDATION, c7);
		
		collaborateurRepository.save(c7);

		
	}
	
	private void addAbsence(LocalDate debut, LocalDate fin, AbsenceStatut statut, Collaborateur collab) {
		Absence a = new Absence();
		a.setDateDebut(debut);
		a.setDateFin(fin);
		a.setType(AbsenceType.CONGE_PAYE);
		a.setStatut(statut);
		a.setCollaborateur(collab);
		absenceRepository.save(a);
	}
	

	/**
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	private void setStatus(HttpStatus status) {
		this.status = status;
	}

	/**
	 * Cette méthode permet de récuperer tous les sulbaternes
	 * à partir d'une liste de matricules
	 *
	 * @param matricules la liste de matricules
	 * @param collabs la liste des collaborateurs
	 * 			
	 * @return retourne la liste des subalternes
	 */
	private List<Collaborateur> getAllCollaborateurSubalternes(List<String> matricules, List<Collaborateur> collabs){
		List<Collaborateur> subalternes = new ArrayList<>();

		matricules.forEach(u -> {
			Collaborateur col = collabs.stream().filter(c -> c.getMatricule().equals(u))
			.findFirst().get();

			subalternes.add(col);
		});
		
		return subalternes;
	}
	
	
	/**
	 * Cette méthode permet d'attribuer le role admin à un manager
	 * 
	 * @param collaborateurs la liste des collaborateurs
	 */
	private void setDefaultAdmin(List<Collaborateur> collaborateurs) {
		Optional<Collaborateur> aManager = collaborateurs.stream().filter(col -> col.getRoles().contains(RoleType.ROLE_MANAGER)).findFirst();
		
		if(aManager.isPresent()) {
			Collaborateur c = aManager.get();
			LOG.info("Set " + c.getPrenom() + ' ' + c.getNom() + " as default admin");
			List<RoleType> roles = new ArrayList<RoleType>(c.getRoles());
			roles.add(RoleType.ROLE_ADMIN);
			c.setRoles(roles);
		}
	}
	
}
