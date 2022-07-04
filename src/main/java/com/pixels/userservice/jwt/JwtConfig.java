package com.pixels.userservice.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtConfig {
	@Value("${application.jwt.secretKey}")
	private String secretKey;
	@Value("${application.jwt.tokenPrefix}")
	private String tokenPrefix;
	@Value("${application.jwt.tokenExpirationAfterDays}")
	private Integer tokenExpirationAfterDays;

	public JwtConfig() {

	}

	@Bean
	public SecretKey getSecretKeySigned() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String getAuthorizationheader() {
		return HttpHeaders.AUTHORIZATION;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public Integer getTokenExpirationAfterDays() {
		return tokenExpirationAfterDays;
	}

	public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
		this.tokenExpirationAfterDays = tokenExpirationAfterDays;
	}

}
