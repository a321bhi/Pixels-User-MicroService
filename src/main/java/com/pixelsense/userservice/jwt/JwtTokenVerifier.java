package com.pixelsense.userservice.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pixelsense.userservice.security.CustomUserDetailsService;
import com.pixelsense.userservice.security.UserDetailsImplementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter {


	
	private CustomUserDetailsService customUserDetailsService;
	private JwtConfig jwtConfig;

	private AuthenticationManager authenticationManager;
	
	public JwtTokenVerifier(AuthenticationManager authenticationManager,JwtConfig jwtConfig,CustomUserDetailsService customUserDetailsService) {
		
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authorizationHeader.replace("Bearer ", "");
		try {


			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtConfig.getSecretKeySigned()).build().parseClaimsJws(token);
			Claims body = claimsJws.getBody();
			String username = body.getSubject();
//			var Set<SimpleGrantedAuthority> simpleGrantedAuthorities = (List<Map<String, String>>) body.get("authorities");

//			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
//					.map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());
//	
//			
//			
//			Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, null,
//					//simpleGrantedAuthorities));
//					Collections.singleton(new SimpleGrantedAuthority("USER"))));
//			UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username, null,Collections.singleton(new SimpleGrantedAuthority("USER")));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}
		filterChain.doFilter(request, response);
	}

}
