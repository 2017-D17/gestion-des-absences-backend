package dev.gda.api.modelview;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.gda.api.entite.RoleType;

public class CollaborateurView{

	private String matricule;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private String departement;
	private Integer conges;
	private Integer rtt;
	private List<RoleType> roles = new ArrayList<RoleType>();
	private List<String> subalternes = new ArrayList<String>();

	public CollaborateurView() {
		super();
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
	 * @return the roles
	 */
	public List<RoleType> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<RoleType> roles) {
		this.roles = roles;
	}

	/**
	 * @return the subalternes
	 */
	public List<String> getSubalternes() {
		return subalternes;
	}

	/**
	 * @param subalternes the subalternes to set
	 */
	public void setSubalternes(List<String> subalternes) {
		this.subalternes = subalternes;
	}
}