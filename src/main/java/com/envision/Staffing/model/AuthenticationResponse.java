package com.envision.Staffing.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private final String jwt;
    private final User user;

    public AuthenticationResponse(String jwt, User user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

	public User getUser() {
		return user;
	}
  
}
