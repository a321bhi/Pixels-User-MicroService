package com.pixelsense.userservice.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.pixelsense.userservice.jwt.JwtConfig;
import com.pixelsense.userservice.jwt.JwtTokenVerifier;
import com.pixelsense.userservice.jwt.JwtUsernameAndPasswordAuthenticatorFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	JwtConfig jwtConfig;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
				http.csrf().disable().cors().and().headers().frameOptions().disable()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.and().authorizeRequests()
						.antMatchers("/user/login", "/user/register", "/user/check/*").permitAll()
						.antMatchers("*").hasAnyAuthority("USER","User","*")
						.anyRequest().authenticated()
				.and().addFilter(new JwtUsernameAndPasswordAuthenticatorFilter(authenticationManager()))
				.addFilterAfter(new JwtTokenVerifier(authenticationManager(),jwtConfig,customUserDetailsService),JwtUsernameAndPasswordAuthenticatorFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		daoProvider.setUserDetailsService(customUserDetailsService);
		return daoProvider;
	}

}