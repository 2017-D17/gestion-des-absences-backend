package dev.gda.api.entite;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


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
	private String email;
	private String password;
	private String departement;
	private Integer conges;
	private Integer rtt;
	private Integer rttEmployeur;
	
	@JsonIgnore
	@Column(name="actif")
	private Boolean actif;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<RoleType> roles = new ArrayList<>();

	@OneToMany
	List<Collaborateur> subalternes = new ArrayList<>();

	
	public Collaborateur() {
		super();
		this.conges = 25;
		this.rtt = 6;
		this.rttEmployeur = 4;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the password
	 */
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the actif
	 */
	public Boolean getActif() {
		return actif;
	}

	/**
	 * @param actif the actif to set
	 */
	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	/**
	 * @return the roles
	 */
	public List<RoleType> getRoles() {
		return unmodifiableList(roles);
	}

	/**
	 * @param role the roles to set
	 */
	public void setRoles(List<RoleType> roles) {
		Assert.notEmpty(roles, "A collab must contain at least one role");
		this.roles = roles;
	}
	
	/**
	 * @return the subalternes
	 */
	public List<Collaborateur> getSubalternes() {
		return unmodifiableList(subalternes);
	}

	/**
	 * @param subalternes the subalternes to set
	 */
	public void setSubalternes(List<Collaborateur> subalternes) {
		if(subalternes != null) {
			this.subalternes = subalternes;
		}else {
			this.subalternes = new ArrayList<>();
		}
	}

}
