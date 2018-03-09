package dev.gda.api.service;

import static dev.gda.api.util.ModelViewUtils.collaborateurView2Collaborateur;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.CollaborateurView;
import dev.gda.api.repository.CollaborateurRepository;

@Service
@Profile("production")
public class InitialiserDonneesServiceImpl implements InitialiserDonneesService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
		
	@Autowired
	private CollaborateurRepository collaborateurRepository;

	@Value("${gda.server.users.url}")
	private String server;
	
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;
	
	public InitialiserDonneesServiceImpl() {
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
			.map(collaborateurView2Collaborateur)
			.forEach(this.collaborateurRepository::save);
		
		List<Collaborateur> collabsFromDb = collaborateurRepository.findAll();
		
		utilisateurs.stream()
		.filter(u -> !u.getSubalternes().isEmpty())
		.forEach(u -> {
			Collaborateur col;
			col = collabsFromDb.stream()
					.filter(c -> c.getMatricule().equals(u.getMatricule()))
					.findFirst().get();

			col.setSubalternes(getAllCollaborateurSubalternes(u.getSubalternes(), collabsFromDb));
			
		});
		
		setDefaultAdmin(collabsFromDb);
		
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
//		List<Collaborateur> subalternes = new ArrayList<>();

		return collabs.stream()
			.filter(c -> matricules.contains(c.getMatricule()))
			.collect(toList());
		
//		matricules.forEach(u -> {
//			Collaborateur col = collabs.stream().filter(c -> c.getMatricule().equals(u))
//			.findFirst().get();
//
//			subalternes.add(col);
//		});
		
//		return subalternes;
	}
	
	
	/**
	 * Cette méthode permet d'attribuer le role admin à un manager
	 * 
	 * @param collaborateurs la liste des collaborateurs
	 */
	private void setDefaultAdmin(List<Collaborateur> collaborateurs) {
		Optional<Collaborateur> aCollab = collaborateurs.stream().filter(col -> col.getRoles().contains(RoleType.ROLE_USER) && !col.getRoles().contains(RoleType.ROLE_MANAGER)).findAny();
		
		if(aCollab.isPresent()) {
			Collaborateur c = aCollab.get();
			LOG.info("Set " + c.getPrenom() + ' ' + c.getNom() + " as default admin");
			List<RoleType> roles = new ArrayList<RoleType>(c.getRoles());
			roles.add(RoleType.ROLE_ADMIN);
			c.setRoles(roles);
		}
	}
}
