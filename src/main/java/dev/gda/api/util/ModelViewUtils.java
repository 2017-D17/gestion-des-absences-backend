package dev.gda.api.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.AbsenceView;
import dev.gda.api.modelview.CollaborateurView;

public class ModelViewUtils {

	/**
	 * Cette méthode permet de mapper le model CollaborateurView en une entité Collaborateur
	 *  
	 * @param collaborateurView le model à mapper
	 * @return
	 */
	public static Collaborateur CollaborateurViewToCollaborateur(CollaborateurView collaborateurView) {
		Collaborateur collaborateur = new Collaborateur();
		collaborateur.setMatricule(collaborateurView.getMatricule());
		collaborateur.setNom(collaborateurView.getNom());
		collaborateur.setPrenom(collaborateurView.getPrenom());
		collaborateur.setEmail(collaborateurView.getEmail());
		collaborateur.setPassword(collaborateurView.getPassword());
		collaborateur.setDepartement(collaborateurView.getDepartement());
		collaborateur.setActif(Boolean.TRUE);
		
		if(collaborateurView.getSubalternes().isEmpty()) {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER));
		}else {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER,RoleType.ROLE_MANAGER));
		}
		
		return collaborateur;
	}
	
	/**
	 * Cette méthode permet de mapper une entité Collaborateur en un model CollaborateurView
	 *  
	 * @param collaborateur le collaborateur à mapper
	 * @return
	 */
	public static CollaborateurView CollaborateurToCollaborateurView(Collaborateur collaborateur) {
		CollaborateurView view = new CollaborateurView();
		

		view.setMatricule(collaborateur.getMatricule());
		view.setNom(collaborateur.getNom());
		view.setPrenom(collaborateur.getPrenom());
		view.setEmail(collaborateur.getEmail());
		view.setDepartement(collaborateur.getDepartement());
		view.setRoles(collaborateur.getRoles());
		view.setConges(collaborateur.getConges());
		view.setRtt(collaborateur.getRtt());
		if(!collaborateur.getSubalternes().isEmpty()) {
			
			view.setSubalternes(
				collaborateur.getSubalternes().stream()
				.map(col -> col.getMatricule())
				.collect(Collectors.toList())
			);
		}
		
		return view;
	}
	
	/**
	 * Cette méthode permet de mapper une entité Absence en un model AbsenceView
	 * @param absence l'absence à mapper
	 * @return 
	 */
	public static AbsenceView AbsenceToAbsenceView(Absence absence) {
		AbsenceView view = new AbsenceView();
		view.setId(absence.getId());
		view.setDateDebut(absence.getDateDebut());
		view.setDateFin(absence.getDateFin());
		view.setMotif(absence.getMotif());
		view.setStatut(absence.getStatut());
		view.setType(absence.getType());
		view.setCollaborateur(CollaborateurToCollaborateurView(absence.getCollaborateur()));
		return view;
	}
	
}
