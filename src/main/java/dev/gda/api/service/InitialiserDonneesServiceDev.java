package dev.gda.api.service;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.Statut;

@Service
public class InitialiserDonneesServiceDev implements InitialiserDonneesService {

	@PersistenceContext
	EntityManager em;
	
	public InitialiserDonneesServiceDev(){
		
	}

	@Override
	@Transactional
	public void initialiser() {
		
		
		Collaborateur c = new Collaborateur();
		c.setMatricule("UUID1");
		c.setNom("jean");
		c.setPrenom("malin");
		em.persist(c);
		
		c = new Collaborateur();
		c.setMatricule("UUID2");
		c.setNom("christophe");
		c.setPrenom("jacques");
		em.persist(c);
		
		c = new Collaborateur();
		c.setMatricule("UUID3");
		c.setNom("annabelle");
		c.setPrenom("melissa");
		em.persist(c);
		
		Absence a = new Absence();
		a.setDateDebut(LocalDate.of(2018, 01, 19));
		a.setDateFin(LocalDate.of(2018, 01, 19));
		a.setStatut(Statut.INITIALE);
		a.setCollaborateur(c);
		em.persist(a);
		
		a = new Absence();
		a.setDateDebut(LocalDate.of(2018, 03, 19));
		a.setDateFin(LocalDate.of(2018, 03, 19));
		a.setStatut(Statut.INITIALE);
		a.setCollaborateur(c);
		em.persist(a);
		
		
		a = new Absence();
		a.setDateDebut(LocalDate.of(2018, 01, 28));
		a.setDateFin(LocalDate.of(2018, 01, 28));
		a.setStatut(Statut.EN_ATTENTE_VALIDATION);
		a.setCollaborateur(c);
		em.persist(a);
	}

}
