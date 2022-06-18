package com.pixelsense.userservice.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pixelsense.userservice.model.PixelSenseUser;

public class UserDetailsImplementation implements UserDetails {
	
	private PixelSenseUser pixelSenseUser;
	
	
	public UserDetailsImplementation(PixelSenseUser pixelSenseUser) {
		super();
		this.pixelSenseUser = pixelSenseUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return pixelSenseUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return pixelSenseUser.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
