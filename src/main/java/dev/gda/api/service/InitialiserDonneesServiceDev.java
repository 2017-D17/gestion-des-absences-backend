package dev.gda.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
import dev.gda.api.entite.Utilisateur;
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class InitialiserDonneesServiceDev implements InitialiserDonneesService {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;

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

	public Utilisateur[] getCollaborateursFromServeur(String uri) {
	    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
	    
	    ResponseEntity<Utilisateur[]> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity,Utilisateur[].class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	  }
	
	@Override
	@Transactional
	public void initialiser() {
		
		List<Utilisateur> utilisateurs = Arrays.asList(getCollaborateursFromServeur("/collaborateurs"));	
		utilisateurs.stream()
			.map(this::utilisateurToCollaborateurMapper)
			.forEach(em::persist);
		
		List<Collaborateur> collabsFromDb = collaborateurRepository.findAll();
		
		utilisateurs.stream().forEach(u -> {
			Collaborateur col;
			if(!u.getSubalternes().isEmpty()) {
				col = collabsFromDb.stream()
						.filter(c -> c.getMatricule().equals(u.getMatricule()))
						.findFirst().get();

				col.setSubalternes(getAllCollaborateurSubalternes(u.getSubalternes(), collabsFromDb));
			}
		});
				
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
	 * Cette méthode permet de mapper une entité Utilisateur en une entité Collaborateur
	 *  
	 * @param utilisateur l'utilisateur à mapper
	 * @return
	 */
	private Collaborateur utilisateurToCollaborateurMapper(Utilisateur utilisateur) {
		Collaborateur collaborateur = new Collaborateur();
		collaborateur.setMatricule(utilisateur.getMatricule());
		collaborateur.setNom(utilisateur.getNom());
		collaborateur.setPrenom(utilisateur.getPrenom());
		collaborateur.setEmail(utilisateur.getEmail());
		collaborateur.setPassword(utilisateur.getPassword());
		collaborateur.setDepartement(utilisateur.getDepartement());
		collaborateur.setActif(Boolean.TRUE);
		
		if(utilisateur.getSubalternes().isEmpty()) {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER));
		}else {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER,RoleType.ROLE_MANAGER));
		}
		
		return collaborateur;
	}
}
