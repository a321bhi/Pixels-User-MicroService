//package com.pixelsense.userservice.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.pixelsense.userservice.jwt.JwtTokenVerifier;
//import com.pixelsense.userservice.jwt.JwtUsernameAndPasswordAuthenticatorFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
//	@Autowired
//	private CustomUserDetailsService customUserDetailsService;
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers("/user/login", "/user/register", "/user/check/*").permitAll()
////                .antMatchers(UrlMapping.VALIDATE_JWT).permitAll()
////                .antMatchers("/api/test/**").permitAll()
//                .anyRequest().authenticated();
//        
//        http.addFilterBefore(new JwtUsernameAndPasswordAuthenticatorFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(new JwtTokenVerifier(),JwtUsernameAndPasswordAuthenticatorFilter.class);
//        return http.build();
//    }
//
//
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
//		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
//		daoProvider.setUserDetailsService(customUserDetailsService);
//		return daoProvider;
//	}
//}