package com.envision.Staffing.model;

import java.io.Serializable;
import java.util.Set;

public class AuthenticationResponse implements Serializable {

	private String accessToken;
	private String refreshToken;
	private Set<String> roles;
	private String id;
	private String name;
	private String email;

	public AuthenticationResponse(String accessToken, String refreshToken, Set<String> roles, String id, String name,
			String email) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.roles = roles;
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AuthenticationResponse(String jwt) {
		this.accessToken = jwt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
