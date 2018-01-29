package dev.gda.api.modelview;

import java.time.LocalDate;

import dev.gda.api.entite.AbsenceStatut;
import dev.gda.api.entite.AbsenceType;


public class AbsenceView {


	private Integer id;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	private String motif;
	private AbsenceType type;
	private AbsenceStatut statut;
	
	private CollaborateurView collaborateur;
	
	public AbsenceView() {
		super();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the dateDebut
	 */
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return the dateFin
	 */
	public LocalDate getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return the motif
	 */
	public String getMotif() {
		return motif;
	}

	/**
	 * @param motif the motif to set
	 */
	public void setMotif(String motif) {
		this.motif = motif;
	}

	/**
	 * @return the type
	 */
	public AbsenceType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AbsenceType type) {
		this.type = type;
	}

	/**
	 * @return the statut
	 */
	public AbsenceStatut getStatut() {
		return statut;
	}

	/**
	 * @param statut the statut to set
	 */
	public void setStatut(AbsenceStatut statut) {
		this.statut = statut;
	}

	/**
	 * @return the collaborateur
	 */
	public CollaborateurView getCollaborateur() {
		return collaborateur;
	}

	/**
	 * @param collaborateur the collaborateur to set
	 */
	public void setCollaborateur(CollaborateurView collaborateur) {
		this.collaborateur = collaborateur;
	}
	
}
