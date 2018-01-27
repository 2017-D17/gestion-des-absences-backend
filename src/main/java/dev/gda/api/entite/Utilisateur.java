package dev.gda.api.entite;

import java.util.List;

public class Utilisateur{

	private String matricule;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private String departement;
	
	private List<String> subalternes;

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
	public String getPassword() {
		return password;
	}



	/**
	 * @param password the password to set
	 */
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