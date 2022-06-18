package com.pixelsense.userservice.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixelsense.userservice.exception.IncorrectPasswordException;
import com.pixelsense.userservice.exception.UserNameNotFoundException;
import com.pixelsense.userservice.exception.UserSearchEmptyResult;
import com.pixelsense.userservice.model.PixelSenseUser;
import com.pixelsense.userservice.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@GetMapping("/all")
	public ArrayList<PixelSenseUser> getAllUsers() {
		ArrayList<PixelSenseUser> searchResult = (ArrayList<PixelSenseUser>) userService.findAllUsers();
		if (searchResult.isEmpty()) {
			throw new UserSearchEmptyResult();
		} else {
			return searchResult;
		}
	}

	@GetMapping("/check/{username}")
	public Boolean userNameExistsCheck(@PathVariable String username) {
		Optional<PixelSenseUser> opt = userService.findUser(username);
		if (!opt.isPresent()) {
			return false;
		}
		return true;
	}

	@PostMapping("/login")
	public PixelSenseUser login(@RequestBody PixelSenseUser user) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		Optional<PixelSenseUser> opt = userService.findUser(user.getUserName());
		if (!opt.isPresent()) {
			throw new UserNameNotFoundException();
		}
		//if (opt.get().getPassword().equals(user.getPassword())) {
		if (bCryptPasswordEncoder.matches(user.getPassword(),opt.get().getPassword())) {
			return opt.get();
		} else {
			throw new IncorrectPasswordException();
		}
	}

//
//	@GetMapping("/{userName}")
//	public PixelSenseUser getUserInfo(@PathVariable String userName) {
//		Optional<PixelSenseUser> opt = userService.findUser(userName);
//		if(!opt.isPresent()) {
//			throw new UserNameNotFoundException();
//		}
//		return opt.get();
//	}

	// REST End point local
	@PostMapping("/register")
	public ResponseEntity<String> addUser(@RequestBody PixelSenseUser user) {
		// Encoding Password
//		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
//		user.setPassword(pbkdf2PasswordEncoder.encode(user.getPassword()));
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		// Extracting First/Middle/Last names
		String[] arrayOfName = user.getFullName().split(" ", 0);
		int numPartOfName = arrayOfName.length;
		user.setFirstName(arrayOfName[0]);
		user.setMiddleName("");
		if (numPartOfName > 1) {
			user.setLastName(arrayOfName[numPartOfName - 1]);
			int mid = numPartOfName - 2;
			int i = 1;
			while (mid > 0) {
				user.setMiddleName(user.getMiddleName() + arrayOfName[i]);
				mid -= 1;
				i += 1;
			}
		} else {
			user.setLastName(arrayOfName[numPartOfName - 1]);
		}
		
		// Saving to database
		userService.addUser(user);
		return new ResponseEntity<String>(user.getFirstName() + " data has been added", HttpStatus.OK);
	}

	@DeleteMapping("/delete/{userName}")
	public ResponseEntity<String> deleteUser(@PathVariable String userName) {
		userService.deleteUser(userName);
		return new ResponseEntity<String>(userName + " removed successfully", HttpStatus.OK);
	}

	@PutMapping("/update/{userName}")
	public ResponseEntity<String> updateUser(@PathVariable String userName, @RequestBody PixelSenseUser updatedUser) {
		userService.updateUser(userName, updatedUser);
		return new ResponseEntity<String>(updatedUser.getFirstName() + "'s data has been updated", HttpStatus.OK);
	}

}
