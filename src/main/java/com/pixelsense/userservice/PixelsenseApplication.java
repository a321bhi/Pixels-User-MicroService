package com.pixelsense.userservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.pixelsense.userservice.model.Authority;

@SpringBootApplication
@EnableEurekaClient
public class PixelsenseApplication {
	
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
	   return WebClient.builder();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PixelsenseApplication.class, args);
	}

	@PostConstruct
	protected void init() {

		List<Authority> authorityList = new ArrayList<>();
		authorityList.add(createAuthority("USER", "User role"));
		// authorityList.add(createAuthority("ADMIN","Admin role"));

	}

	private Authority createAuthority(String roleCode, String roleDescription) {
		Authority authority = new Authority();
		authority.setRoleCode(roleCode);
		authority.setRoleDescription(roleDescription);
		return authority;
	}

}
