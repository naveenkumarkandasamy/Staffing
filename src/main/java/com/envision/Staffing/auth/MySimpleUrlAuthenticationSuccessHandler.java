package com.envision.Staffing.auth;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.envision.Staffing.model.AuthenticationResponse;
import com.envision.Staffing.model.User;
import com.envision.Staffing.services.JwtUtil;
import com.envision.Staffing.services.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.toSet());
		User user = new User(authentication.getName(), roles);
		response.getWriter().write(new ObjectMapper().writeValueAsString(user));
		response.setStatus(200);
		System.out.println("Hi");
	}
}