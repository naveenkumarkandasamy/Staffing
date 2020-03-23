package com.envision.Staffing.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.User;
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
    
    public User getUser(String s) throws UsernameNotFoundException{
    	User user = this.userPrincipalRepository.findById(s);
    	return user;
    }
	
}