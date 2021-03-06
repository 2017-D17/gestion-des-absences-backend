package dev.gda.api.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class InitialiserDonneesServiceDev implements InitialiserDonneesService {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;

	private String server = "https://collab-json-api.herokuapp.com";
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;
	
	public InitialiserDonneesServiceDev() {
		this.rest = new RestTemplate();
		this.headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "*/*");
	}

	public Collaborateur[] getCollaborateursFromServeur(String uri) {
	    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
	    
	    ResponseEntity<Collaborateur[]> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, Collaborateur[].class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	  }
	
	@Override
	@Transactional
	public void initialiser() {
		
		List<Collaborateur> collabs = Arrays.asList(getCollaborateursFromServeur("/collaborateurs"));
		
		collabs.stream().forEach(em::persist);
		
		List<Collaborateur> collabsFromDb = collaborateurRepository.findAll();
		Collaborateur c = collabsFromDb.get(2);
		

		addAbsence(LocalDate.of(2018, 01, 19),LocalDate.of(2018, 01, 19), AbsenceStatut.INITIALE, c);
		addAbsence(LocalDate.of(2018, 01, 28),LocalDate.of(2018, 01, 28), AbsenceStatut.INITIALE, c);
		c.setConges(c.getConges() - 1);
		addAbsence(LocalDate.of(2018, 03, 19),LocalDate.of(2018, 03, 19), AbsenceStatut.EN_ATTENTE_VALIDATION, c);
		em.persist(c);
		
	}
	
	private void addAbsence(LocalDate debut, LocalDate fin, AbsenceStatut statut, Collaborateur collab) {
		Absence a = new Absence();
		a.setDateDebut(debut);
		a.setDateFin(fin);
		a.setType(AbsenceType.CONGE_PAYE);
		a.setStatut(statut);
		a.setCollaborateur(collab);
		em.persist(a);
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

}
