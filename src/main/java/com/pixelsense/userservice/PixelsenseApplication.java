package com.pixelsense.userservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pixelsense.userservice.model.Authority;

@SpringBootApplication
public class PixelsenseApplication {

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
