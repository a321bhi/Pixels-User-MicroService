package com.pixels.userservice.service;

import java.util.List;
import java.util.Optional;

import com.pixels.userservice.model.PixelSenseUser;

public interface UserService {
	public void addUser(PixelSenseUser user);

	public List<PixelSenseUser> findAllUsers();

	public Optional<PixelSenseUser> findUserById(String username);

	public void deleteUserById(String username);

	public void updateUser(String username, PixelSenseUser user);
	public List<String> getUsernameBasedOnSearch(String username);
}
