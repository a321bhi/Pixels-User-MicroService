package com.pixelsense.userservice.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.pixelsense.userservice.doa.UserRepository;
import com.pixelsense.userservice.exception.UserNameNotFoundException;
import com.pixelsense.userservice.model.PixelSenseUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username){
		Optional<PixelSenseUser> pixelSenseUser = userRepository.findById(username);
		if(pixelSenseUser.isEmpty()) {
			throw new UserNameNotFoundException();
		}
		return new UserDetailsImplementation(pixelSenseUser.get());
	}

}
