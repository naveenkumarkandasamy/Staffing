package com.envision.Staffing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MultipleAuthProvidersSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
//	CustomAuthenticationProvider customAuthProvider;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private UserDetailsService myUserDetailsService;

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//		// auth.authenticationProvider(customAuthProvider);
//		auth.inMemoryAuthentication().withUser("osatadmin").password("{noop}osatadmin123").roles("ADMIN").and()
//				.withUser("osatuser").password("{noop}osatuser123").roles("USER");
//	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

//		http.csrf().disable()
//        .exceptionHandling()
//        .and()
//        .authorizeRequests()
//        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//        .antMatchers("/request").authenticated()
//        .and()
//    .formLogin()
//        .loginProcessingUrl("/login")
//        .permitAll()
//        .usernameParameter("username")
//        .passwordParameter("pass")
//        .successHandler(new MySimpleUrlAuthenticationSuccessHandler())
//       // .failureHandler(new MySimpleUrlAuthenticationSuccessHandler())
//        .and()
//        .httpBasic();
////    .logout()
////    .logoutUrl("/logout")
////    .invalidateHttpSession(true);
		httpSecurity.csrf().disable().authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated()
				.and().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
}