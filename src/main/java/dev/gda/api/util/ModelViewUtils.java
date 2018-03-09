package dev.gda.api.util;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.gda.api.entite.Absence;
import dev.gda.api.entite.Collaborateur;
import dev.gda.api.entite.RoleType;
import dev.gda.api.modelview.AbsenceView;
import dev.gda.api.modelview.CollaborateurView;

public class ModelViewUtils {

	/**
	 * Permet de mapper le model CollaborateurView en une entité Collaborateur
	 */
	public static Function<CollaborateurView, Collaborateur> collaborateurView2Collaborateur = view -> {
		Collaborateur collaborateur = new Collaborateur();
		collaborateur.setMatricule(view.getMatricule());
		collaborateur.setNom(view.getNom());
		collaborateur.setPrenom(view.getPrenom());
		collaborateur.setEmail(view.getEmail());
		collaborateur.setPassword(view.getPassword());
		collaborateur.setDepartement(view.getDepartement());
		collaborateur.setActif(Boolean.TRUE);
		
		if(view.getSubalternes().isEmpty()) {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER));
		}else {
			collaborateur.setRoles(Arrays.asList(RoleType.ROLE_USER,RoleType.ROLE_MANAGER));
		}
		
		return collaborateur;
	};
	
	/**
	 * Permet de mapper une entité Collaborateur en un model CollaborateurView  
	 */
	public static Function<Collaborateur, CollaborateurView>  collaborateur2CollaborateurView = collaborateur -> {
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
	};
	
	/**
	 * Permet de mapper une entité Absence en un model AbsenceView
	 */
	public static Function<Absence, AbsenceView> absence2AbsenceView = absence -> {
		AbsenceView view = new AbsenceView();
		view.setId(absence.getId());
		view.setDateDebut(absence.getDateDebut());
		view.setDateFin(absence.getDateFin());
		view.setMotif(absence.getMotif());
		view.setStatut(absence.getStatut());
		view.setType(absence.getType());
		view.setCollaborateur(collaborateur2CollaborateurView.apply(absence.getCollaborateur()));
		return view;
	};
	
	
}
