package com.envision.Staffing.auth;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.envision.Staffing.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles = authentication.getAuthorities().stream()
			     .map(r -> r.getAuthority()).collect(Collectors.toSet());
		User user = new User(authentication.getName(), roles);
		// TODO Auto-generated method stub
		System.out.println(authentication.getAuthorities());
		  response.getWriter().write(new ObjectMapper().writeValueAsString(user));
          response.setStatus(200);
		System.out.println("Hi");

	}
}