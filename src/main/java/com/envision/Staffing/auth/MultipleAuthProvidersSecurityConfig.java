package com.envision.Staffing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MultipleAuthProvidersSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	CustomAuthenticationProvider customAuthProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		//auth.authenticationProvider(customAuthProvider);
		auth.inMemoryAuthentication().withUser("osatadmin").password("{noop}osatadmin123").roles("ADMIN")
		.and()
		.withUser("osatuser").password("{noop}osatuser123").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
        .exceptionHandling()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .antMatchers("/request").authenticated()
        .and()
    .formLogin()
        .loginProcessingUrl("/login")
        .permitAll()
        .usernameParameter("username")
        .passwordParameter("pass")
        .successHandler(new MySimpleUrlAuthenticationSuccessHandler())
       // .failureHandler(new MySimpleUrlAuthenticationSuccessHandler())
        .and()
        .httpBasic();
//    .logout()
//    .logoutUrl("/logout")
//    .invalidateHttpSession(true);

	}

//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
}