package com.envision.Staffing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class MultipleAuthProvidersSecurityConfig 
  extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomAuthenticationProvider customAuthProvider;
 
    @Override
    public void configure(AuthenticationManagerBuilder auth) 
      throws Exception {
 
        auth.authenticationProvider(customAuthProvider);
        auth.inMemoryAuthentication()
            .withUser("osatadmin")
            .password("{noop}osatadmin123")
            .roles("ADMIN");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated();
        http.csrf().disable();    
    }
     
      
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
}