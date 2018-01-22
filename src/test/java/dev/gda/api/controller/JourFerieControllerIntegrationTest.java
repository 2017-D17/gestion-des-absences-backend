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

import dev.gda.api.entite.JourFerie;
import dev.gda.api.entite.JourFerieType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JourFerieControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test_scenario_jour_feries() {
		// insertion
		JourFerie jf = new JourFerie();
		jf.setDate(LocalDate.now());
		jf.setCommentaire("un commentaire");
		jf.setType(JourFerieType.JOUR_FERIE);
		JourFerie jfr = this.restTemplate.postForObject("/jours_feries", jf, JourFerie.class);

		// vérification insertion
		assertThat(jfr).isNotNull();
		assertThat(jfr.getId()).isNotNull();
		assertThat(jfr.getCommentaire()).isEqualTo(jf.getCommentaire());

		// lister pour vérifier l'insertion
		ResponseEntity<JourFerie[]> re = this.restTemplate.getForEntity("/jours_feries", JourFerie[].class);
		JourFerie[] abs = re.getBody();
		assertThat(abs.length).isEqualTo(1);

		// modification
		jfr.setCommentaire("un nouveau commentaire");

		HttpEntity<JourFerie> requestEntity = new HttpEntity<JourFerie>(jfr);

		ResponseEntity<JourFerie> rep = this.restTemplate.exchange("/jours_feries/" + jfr.getId(), HttpMethod.PUT,
				requestEntity, JourFerie.class);
		JourFerie jfr2 = rep.getBody();
		// vérification modification
		assertThat(jfr2).isNotNull();
		assertThat(jfr2.getId()).isNotNull();
		assertThat(jfr2.getId()).isEqualTo(jfr.getId());
		assertThat(jfr2.getCommentaire()).isEqualTo(jfr.getCommentaire());

		// lister pour vérifier modification
		ResponseEntity<JourFerie[]> re2 = this.restTemplate.getForEntity("/jours_feries", JourFerie[].class);
		JourFerie[] abs2 = re2.getBody();
		assertThat(abs2.length).isEqualTo(1);

	}

}