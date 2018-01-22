package dev.gda.api.controller;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbsenceControllerIntegrationTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	

	@Test
	public void test_scenario_crud_lister_absences() {
		// inserer
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
		
		ResponseEntity<Absence[]> responseEntity = this.restTemplate.getForEntity("/absences/8b2d3ac7", Absence[].class);
		Absence[] absences = responseEntity.getBody();
		assertThat(absences.length).isEqualTo(4);
		
		
		
	}
}
