package dev.gda.api.entite;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Cette classe represente l'entité absence
 * 
 * Une absence est associé à un collaborateur
 * 
 * Une absence se définit par :
 *    - une date de début 
 *    - une date de fin
 *    - un motif
 * 
 */
@Entity
public class Absence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private LocalDate dateDebut;
	@NotNull
	private LocalDate dateFin;
	private String motif;
	@NotNull
	private AbsenceType type;
	private AbsenceStatut statut;
	
	@ManyToOne
	private Collaborateur collaborateur;
	
	public Absence() {
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
	public Collaborateur getCollaborateur() {
		return collaborateur;
	}

	/**
	 * @param collaborateur the collaborateur to set
	 */
	public void setCollaborateur(Collaborateur collaborateur) {
		this.collaborateur = collaborateur;
	}

}
