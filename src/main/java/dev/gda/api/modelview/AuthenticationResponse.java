package dev.gda.api.modelview;

public class AuthenticationResponse {

	private String token;
	private CollaborateurView collaborateur;
	
	
	public AuthenticationResponse() {
		super();
	}
	
	public AuthenticationResponse(String token, CollaborateurView collaborateur) {
		super();
		this.token = token;
		this.collaborateur = collaborateur;
	}


	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}


	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
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
