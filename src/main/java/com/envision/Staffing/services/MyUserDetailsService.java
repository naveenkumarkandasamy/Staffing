package com.envision.Staffing.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.User;
import com.envision.Staffing.model.UserAuth;
import com.envision.Staffing.model.UserPrincipal;
import com.envision.Staffing.repository.UserPrincipalRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private UserPrincipalRepository userPrincipalRepository;

	public MyUserDetailsService(UserPrincipalRepository userPrincipalRepository) {
		this.userPrincipalRepository = userPrincipalRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = this.userPrincipalRepository.findById(s);
		UserPrincipal userPrincipal = new UserPrincipal(user);
		return userPrincipal;
	}

	public UserAuth getUser(String s) throws UsernameNotFoundException {

		User user = this.userPrincipalRepository.findById(s);
		String role = null;
		if (user.getName().equals("USER")) {
			role = "ROLE_USER";
		} else if (user.getName().equals("ADMIN")) {
			role = "ROLE_ADMIN";
		}
		Set<String> roles = new HashSet<>(Arrays.asList(role));
		UserAuth userauth = new UserAuth();
		userauth.setId(null);
		userauth.setEmail(user.getEmail());
		userauth.setRoles(roles);
		userauth.setName(user.getId());
		return userauth;

	}

}