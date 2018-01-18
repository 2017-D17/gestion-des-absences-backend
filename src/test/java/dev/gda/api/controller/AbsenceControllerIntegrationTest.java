package dev.gda.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import dev.gda.api.entite.Absence;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbsenceControllerIntegrationTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	

	@Test
	public void test_scenario_crud_lister_absences() {
		
		// inserer
		
		// lister pour vérifier l'insertion
		
		ResponseEntity<Absence[]> responseEntity = this.restTemplate.getForEntity("/absences/UUID3", Absence[].class);
		
		Absence[] absences = responseEntity.getBody();
		// envoyer une requête http
		assertThat(absences.length).isEqualTo(3);
		
		// vérifier le résultat
		
	}
}
