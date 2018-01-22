package dev.gda.api.entite;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ADMIN, MANAGER, EMPLOYE;
	
	public GrantedAuthority getAuthority() {
		System.out.println("-----------------------------getAuthority");
		return new SimpleGrantedAuthority(this.name());
	}
}
