package com.pixelsense.pixelsense.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixelsense.pixelsense.model.PixelSenseUser;
import com.pixelsense.pixelsense.repository.UserRepository;

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
		return (List<PixelSenseUser>)userRepository.findAll();
	}

	@Override
	public Optional<PixelSenseUser> findUser(String userName) {
		return userRepository.findById(userName);
	}

	@Override
	public void deleteUser(String userName) {
		userRepository.deleteById(userName);
	}

	@Override
	public void updateUser(String userName, PixelSenseUser user) {
		deleteUser(userName);
		addUser(user);
	}

}
