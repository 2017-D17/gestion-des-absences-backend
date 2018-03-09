 package dev.gda.api.controller;

import static dev.gda.api.util.ModelViewUtils.collaborateurView2Collaborateur;
import static java.util.Arrays.asList;
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
import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.AbsenceView;
import dev.gda.api.modelview.AuthenticationResponse;
import dev.gda.api.repository.CollaborateurRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbsenceControllerIntegrationTest {

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
	    jean.setRoles(asList(RoleType.ROLE_USER));
	    this.collaborateurRepository.save(jean);
	}
	
	@After
	public void tearDown() {
	    collaborateurRepository.delete(jean);
	}
	
	
	@Test
	public void test_scenario_crud_lister_absences() {

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
		Absence ab = new Absence();
		ab.setDateDebut(LocalDate.now().plusDays(2));
		ab.setDateFin(LocalDate.now().plusDays(3));
		ab.setType(AbsenceType.RTT);
		
		HttpEntity<Absence> entity = new HttpEntity<Absence>(ab, headers);
		AbsenceView abr = this.restTemplate.exchange("/absences", HttpMethod.POST, entity, AbsenceView.class).getBody();
		
		assertThat(abr).isNotNull();
		assertThat(abr.getId()).isNotNull();
		assertThat(abr.getMotif()).isEqualTo(ab.getMotif());

		// lister pour vérifier l'insertion
		AbsenceView[] abs = lister("/absences/" + jean.getMatricule(), headers);
		assertThat(abs.length).isEqualTo(1);



		// modification		
		abr.setMotif("un nouveau motif");
		Absence a = AbsenceViewToAbsence(abr);
		
		HttpEntity<Absence> requestEntity = new HttpEntity<Absence>(a, headers);
		ResponseEntity<AbsenceView> rep = this.restTemplate.exchange("/absences/" + abr.getId(), HttpMethod.PUT,
				requestEntity, AbsenceView.class);
		AbsenceView abr2 = rep.getBody();
		// vérification modification
		assertThat(abr2).isNotNull();
		assertThat(abr2.getId()).isNotNull();
		assertThat(abr2.getId()).isEqualTo(abr.getId());
		assertThat(abr2.getMotif()).isEqualTo(abr.getMotif());

		// lister pour vérifier modification
		AbsenceView[] abs2 = lister("/absences/" + jean.getMatricule(), headers);
		assertThat(abs2.length).isEqualTo(1);
		
		
		// suppression
		HttpEntity<AbsenceView> entity2 = new HttpEntity<AbsenceView>(headers);
		this.restTemplate.exchange("/absences/" + abr2.getId(), HttpMethod.DELETE, entity2, AbsenceView.class);
		
		// lister pour vérifier suppression
		AbsenceView[] abs4 = lister("/absences/" + jean.getMatricule(), headers);
		assertThat(abs4.length).isEqualTo(0);

	}
		
	private AbsenceView[] lister(String url, HttpHeaders headers) {
		HttpEntity<AbsenceView> entity = new HttpEntity<AbsenceView>(headers);
		ResponseEntity<AbsenceView[]> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, AbsenceView[].class);
		return response.getBody();	
	}
	
	private Absence AbsenceViewToAbsence(AbsenceView absenceView) {
		Absence absence = new Absence();
		absence.setId(absenceView.getId());
		absence.setDateDebut(absenceView.getDateDebut());
		absence.setDateFin(absenceView.getDateFin());
		absence.setMotif(absenceView.getMotif());
		absence.setStatut(absenceView.getStatut());
		absence.setType(absenceView.getType());
		absence.setCollaborateur(collaborateurView2Collaborateur.apply(absenceView.getCollaborateur()));
		return absence;
	}
	
}
