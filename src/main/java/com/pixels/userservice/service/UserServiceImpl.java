package com.pixels.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixels.userservice.doa.UserRepository;
import com.pixels.userservice.model.PixelSenseUser;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void addUser(PixelSenseUser user) {
		userRepository.save(user);
	}

	@Override
	public List<PixelSenseUser> findAllUsers() {
		return (List<PixelSenseUser>) userRepository.findAll();
	}

	@Override
	public Optional<PixelSenseUser> findUserById(String username) {
		return userRepository.findById(username);
	}

	@Override
	public void deleteUserById(String username) {
		userRepository.deleteById(username);
	}

	@Override
	public void updateUser(String username, PixelSenseUser user) {
		userRepository.deleteById(username);
		userRepository.save(user);
	}
	@Override
	public List<String> getUsernameBasedOnSearch(String queryUsername) {
		return userRepository.getUsernameBasedOnSearch(queryUsername);
	}
}
