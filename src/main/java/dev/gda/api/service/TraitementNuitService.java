package dev.gda.api.service;

import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.repository.AbsenceRepository;
import dev.gda.api.repository.CollaborateurRepository;

@Service
public class TraitementNuitService{
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Autowired
	private CollaborateurRepository collaborateurRepository;
	
	@Autowired
	private EmailService emailService;
		
	@Scheduled(cron = "${gda.traitement.nuit.cron:0 0 0 * * *}", zone="CET")
	public void faireTraitement() {
		this.absenceRepository.findByStatutOrderByIdAsc(AbsenceStatut.INITIALE).stream().forEach(abs -> {
			

			if(abs.getType().equals(AbsenceType.RTT_EMPLOYEUR)) {
				LOG.info("Traitement de la demande rtt employeur pour " + abs.getCollaborateur().getPrenom() +' '+ abs.getCollaborateur().getNom());
				traitementAbsenceTypeRttEmployeur(abs);				
			}else {
				LOG.info("Traitement de la demande faite par "+ abs.getCollaborateur().getPrenom() +' '+ abs.getCollaborateur().getNom());
				traitementAbsenceTypeAutre(abs);
			}
			
			this.absenceRepository.save(abs);
		});
	}
	
	private void traitementAbsenceTypeRttEmployeur(Absence absence) {
		Collaborateur collab = absence.getCollaborateur();
		absence.setStatut(AbsenceStatut.VALIDEE);				
		collab.setRttEmployeur(collab.getRttEmployeur() - 1);
		
	}

	private void traitementAbsenceTypeAutre(Absence absence) {
		Collaborateur collab = absence.getCollaborateur();
		int nombreJours = (int) ChronoUnit.DAYS.between(absence.getDateDebut(), absence.getDateFin());
		if(absence.getType().equals(AbsenceType.RTT)) {
			
			if(collab.getRtt() - nombreJours >= 0 ) {
				collab.setRtt(collab.getRtt() - nombreJours);
				absence.setStatut(AbsenceStatut.EN_ATTENTE_VALIDATION);
			}
		}else {
			if((collab.getConges() - nombreJours) >= 0 ) {
				collab.setConges(collab.getConges() - nombreJours);
				absence.setStatut(AbsenceStatut.EN_ATTENTE_VALIDATION);
			}
			
			if(absence.getStatut().equals(AbsenceStatut.INITIALE) ) {
				absence.setStatut(AbsenceStatut.REJETEE);
			}else {

				Collaborateur manager = this.collaborateurRepository.findManagerMatricule(absence.getCollaborateur().getMatricule())
											.map(collaborateurRepository::findOne).get();
				
				LOG.info("Envoi du mail à "+ manager.getPrenom() +' '+ manager.getNom()+ "au "+ manager.getEmail());
				
				this.emailService.sendSimpleMessage(manager.getEmail(),
							"Nouvelle Demande d'Absence ",
							absence.getCollaborateur().getPrenom() + ' '+ absence.getCollaborateur().getNom() + " a une demande en attente de validation");
			}
		}
	}
}
