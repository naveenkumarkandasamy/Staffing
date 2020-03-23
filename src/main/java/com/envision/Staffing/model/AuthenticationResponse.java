package com.envision.Staffing.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String accessToken;
    private User user;
    private String refreshToken;
    
    public AuthenticationResponse(String jwt) {
		this.accessToken = jwt;
    }

    public AuthenticationResponse(String jwt, User user, String refreshToken) {
		this.refreshToken = refreshToken;
		this.accessToken = jwt;
        this.user = user;
    }

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public User getUser() {
		return user;
	}
  
}
