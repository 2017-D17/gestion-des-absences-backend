package dev.gda.api.auth;

public class AuthCredentials {

	  private String email;
	  private String password;
	  
	  /**
	   * @return the username
	   */
	  public String getEmail() {
	    return email;
	  }
	  /**
	   * @param username the username to set
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
	  
	}
