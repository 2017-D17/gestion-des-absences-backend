package dev.gda.api.entite;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Cette classe represente l'entité collaborateur
 * 
 * Un collaborateur se définit par :
 *    - un matricule
 *    - un nom
 *    - un prenom
 *    - un nombre de jour de congé
 *    - un nombre de rtt
 *    - un nombre de rtt employeur
 * 
 */
@Entity
public class Collaborateur {

	@Id
	private String matricule;
	private String nom;
	private String prenom;
	private Integer conges;
	private Integer rtt;
	private Integer rttEmployeur;
	private String departement;
	private ArrayList<String> subalternes;
	
	public Collaborateur() {
		super();
		this.conges = 25;
		this.rtt = 6;
		this.rttEmployeur = 4;
	}
	
	/**
	 * @return the conges
	 */
	public Integer getConges() {
		return conges;
	}

	/**
	 * @param conges the conges to set
	 */
	public void setConges(Integer conges) {
		this.conges = conges;
	}

	/**
	 * @return the rtt
	 */
	public Integer getRtt() {
		return rtt;
	}

	/**
	 * @param rtt the rtt to set
	 */
	public void setRtt(Integer rtt) {
		this.rtt = rtt;
	}

	/**
	 * @return the rttEmployeur
	 */
	public Integer getRttEmployeur() {
		return rttEmployeur;
	}

	/**
	 * @param rttEmployeur the rttEmployeur to set
	 */
	public void setRttEmployeur(Integer rttEmployeur) {
		this.rttEmployeur = rttEmployeur;
	}
	

	/**
	 * @return the matricule
	 */
	public String getMatricule() {
		return matricule;
	}

	/**
	 * @param matricule the matricule to set
	 */
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the departement
	 */
	public String getDepartement() {
		return departement;
	}

	/**
	 * @param departement the departement to set
	 */
	public void setDepartement(String departement) {
		this.departement = departement;
	}

	/**
	 * @return the subalternes
	 */
	public ArrayList<String> getSubalternes() {
		return subalternes;
	}

	/**
	 * @param subalternes the subalternes to set
	 */
	public void setSubalternes(ArrayList<String> subalternes) {
		this.subalternes = subalternes;
	}


	
	
		
}
