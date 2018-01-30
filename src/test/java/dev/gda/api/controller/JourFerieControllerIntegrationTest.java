package dev.gda.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import dev.gda.api.auth.AuthCredentials;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.JourFerie;
import dev.gda.api.entite.JourFerieType;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.AbsenceView;
import dev.gda.api.modelview.AuthenticationResponse;
import dev.gda.api.repository.CollaborateurRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JourFerieControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CollaborateurRepository collaborateurRepository;
	
	private Collaborateur jean;
	
	
	@Before
	public void setup() {
		jean = new Collaborateur();
	    jean.setMatricule("UUID3");
	    jean.setEmail("jean@mail.com");
	    jean.setPassword("$2a$10$Qse7HJSORoF8Q5lmOO/6OOh1IKhLVqV4BnOaz1E1U//2rH4vPyO9q");
	    jean.getRoles().add(RoleType.ROLE_ADMIN);
	    collaborateurRepository.save(jean);	    
	}
	
	@After
	public void tearDown() {
	    collaborateurRepository.delete(jean);
	}
	
	@Test
	public void test_scenario_jour_feries() {
		
		// Authentification
		AuthCredentials authCredentials = new AuthCredentials();
		authCredentials.setEmail(jean.getEmail());
		authCredentials.setPassword("password");
		AuthenticationResponse auth = this.restTemplate.postForObject("/login", authCredentials, AuthenticationResponse.class);
		assertThat(auth).isNotNull();
		assertThat(auth.getToken()).isNotNull();
		assertThat(auth.getCollaborateur().getEmail()).isEqualTo(authCredentials.getEmail());
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ auth.getToken());
		
		
		// insertion
		JourFerie jf = new JourFerie();
		jf.setDate(LocalDate.now());
		jf.setCommentaire("un commentaire");
		jf.setType(JourFerieType.JOUR_FERIE);
		
		HttpEntity<JourFerie> entity = new HttpEntity<JourFerie>(jf, headers);
		JourFerie jfr = this.restTemplate.exchange("/jours_feries", HttpMethod.POST, entity, JourFerie.class).getBody();

		// vérification insertion
		assertThat(jfr).isNotNull();
		assertThat(jfr.getId()).isNotNull();
		assertThat(jfr.getCommentaire()).isEqualTo(jf.getCommentaire());

		// lister pour vérifier l'insertion
		JourFerie[] abs = lister("/jours_feries", headers);
		assertThat(abs.length).isEqualTo(1);

		// modification
		jfr.setCommentaire("un nouveau commentaire");

		HttpEntity<JourFerie> requestEntity = new HttpEntity<JourFerie>(jfr, headers);

		ResponseEntity<JourFerie> rep = this.restTemplate.exchange("/jours_feries/" + jfr.getId(), HttpMethod.PUT,
				requestEntity, JourFerie.class);
		JourFerie jfr2 = rep.getBody();
		// vérification modification
		assertThat(jfr2).isNotNull();
		assertThat(jfr2.getId()).isNotNull();
		assertThat(jfr2.getId()).isEqualTo(jfr.getId());
		assertThat(jfr2.getCommentaire()).isEqualTo(jfr.getCommentaire());

		// lister pour vérifier modification
		JourFerie[] abs2 = lister("/jours_feries", headers);
		assertThat(abs2.length).isEqualTo(1);

		// suppression
		HttpEntity<JourFerie> entity2 = new HttpEntity<JourFerie>(headers);
		this.restTemplate.exchange("/jours_feries/" + jfr.getId(), HttpMethod.DELETE, entity2, JourFerie.class);
		
		// lister pour vérifier suppression
		JourFerie[] abs3 = lister("/jours_feries", headers);
		assertThat(abs3.length).isEqualTo(0);
	}
	
	private JourFerie[] lister(String url, HttpHeaders headers) {
		HttpEntity<AbsenceView> entity = new HttpEntity<AbsenceView>(headers);
		ResponseEntity<JourFerie[]> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, JourFerie[].class);
		return response.getBody();	
	}

}