package com.envision.Staffing.auth;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.envision.Staffing.Application;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	Logger log = Logger.getLogger(CustomAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		log.info("Entering method to authenticate ::");
		String username = auth.getName();
		String password = auth.getCredentials().toString();

		if ("osatuser".equalsIgnoreCase(username) && "osatuser123".equals(password)) {
			return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
		} else {
			log.error("External system authentication failed");
			throw new BadCredentialsException("External system authentication failed");
		}
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
}