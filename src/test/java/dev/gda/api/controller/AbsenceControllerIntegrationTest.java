package dev.gda.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbsenceControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test_scenario_crud_lister_absences() {

		// insertion
		Absence ab = new Absence();
		ab.setDateDebut(LocalDate.now().plusDays(2));
		ab.setDateFin(LocalDate.now().plusDays(3));
		ab.setType(AbsenceType.RTT);

		Collaborateur c = new Collaborateur();
		// TODO (me) rendre le test indépendant
		c.setMatricule("8b2d3ac7");

		ab.setCollaborateur(c);

		Absence abr = this.restTemplate.postForObject("/absences", ab, Absence.class);
		assertThat(abr).isNotNull();
		assertThat(abr.getId()).isNotNull();
		assertThat(abr.getMotif()).isEqualTo(ab.getMotif());

		// lister pour vérifier l'insertion
		ResponseEntity<Absence[]> re = this.restTemplate.getForEntity("/absences/8b2d3ac7", Absence[].class);
		Absence[] abs = re.getBody();
		assertThat(abs.length).isEqualTo(4);

		// modification
		abr.setMotif("un nouveau motif");

		HttpEntity<Absence> requestEntity = new HttpEntity<Absence>(abr);

		ResponseEntity<Absence> rep = this.restTemplate.exchange("/absences/" + abr.getId(), HttpMethod.PUT,
				requestEntity, Absence.class);
		Absence abr2 = rep.getBody();
		// vérification modification
		assertThat(abr2).isNotNull();
		assertThat(abr2.getId()).isNotNull();
		assertThat(abr2.getId()).isEqualTo(abr.getId());
		assertThat(abr2.getMotif()).isEqualTo(abr.getMotif());

		// lister pour vérifier modification
		ResponseEntity<Absence[]> re2 = this.restTemplate.getForEntity("/absences/8b2d3ac7", Absence[].class);
		Absence[] abs2 = re2.getBody();
		assertThat(abs2.length).isEqualTo(4);

		// modification statut
		abr2.setStatut(AbsenceStatut.EN_ATTENTE_VALIDATION);
	
		Absence abr3 = this.restTemplate.postForObject("/absences/" + abr2.getId() + "?_method=patch", abr2, Absence.class);
		// vérification modification statut
		assertThat(abr3).isNotNull();
		assertThat(abr3.getId()).isNotNull();
		assertThat(abr3.getId()).isEqualTo(abr2.getId());
		assertThat(abr3.getStatut()).isEqualTo(abr2.getStatut());

		// lister pour vérifier modification statut
		ResponseEntity<Absence[]> re3 = this.restTemplate.getForEntity("/absences/8b2d3ac7", Absence[].class);
		Absence[] abs3 = re3.getBody();
		assertThat(abs3.length).isEqualTo(4);

	}
}
