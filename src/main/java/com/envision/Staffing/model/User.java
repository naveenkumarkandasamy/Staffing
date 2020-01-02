package com.envision.Staffing.model;

import java.util.Set;

public class User {
	
	private String userName;
	

	private String status;
	
	private Set<String> roles;

	public User(String status) {
		this.status = status;
	}

	
	public User(String userName, Set<String> roles) {
		super();
		this.userName = userName;
		this.roles = roles;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
