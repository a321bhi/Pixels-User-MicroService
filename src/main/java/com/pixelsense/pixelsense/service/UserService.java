package com.pixelsense.pixelsense.service;

import java.util.List;
import java.util.Optional;

import com.pixelsense.pixelsense.model.PixelSenseUser;


public interface UserService {
	public void addUser(PixelSenseUser user);
	public List<PixelSenseUser> findAllUsers();
	public Optional<PixelSenseUser> findUser(String userName);
	public void deleteUser(String userName);
	public void updateUser(String userName, PixelSenseUser user);
}
