package com.pixels.userservice.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pixels.userservice.model.PixelSenseUser;

public class UserDetailsImplementation implements UserDetails {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private PixelSenseUser pixelsUser;

	public UserDetailsImplementation(PixelSenseUser pixelsUser) {
		super();
		this.pixelsUser = pixelsUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		return pixelsUser.getPassword();
	}

	@Override
	public String getUsername() {
		return pixelsUser.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
