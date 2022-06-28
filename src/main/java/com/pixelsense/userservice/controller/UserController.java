package com.pixelsense.userservice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pixelsense.userservice.doa.UserRepository;
import com.pixelsense.userservice.exception.UserSearchEmptyResult;
import com.pixelsense.userservice.exception.UsernameNotFoundException;
import com.pixelsense.userservice.jwt.JwtConfig;
import com.pixelsense.userservice.model.PixelSenseUser;
import com.pixelsense.userservice.service.UserServiceImpl;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	JwtConfig jwtConfig;

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
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = Jwts.builder().setSubject(username).claim("authorities", "USER").setIssuedAt(new Date())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
				.signWith(jwtConfig.getSecretKeySigned()).compact();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(jwtConfig.getAuthorizationheader(), "Bearer " + token);
		responseHeaders.set("Access-Control-Expose-Headers", jwtConfig.getAuthorizationheader());
		System.out.println("here");
		return ResponseEntity.ok().headers(responseHeaders).body("Logged in");
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
//		Optional<PixelSenseUser> opt = userService.findUser(user.getUserName());
//		if (!opt.isPresent()) {
//			throw new UsernameNotFoundException();
//		}
//		// if (opt.get().getPassword().equals(user.getPassword())) {
//		if (bCryptPasswordEncoder.matches(user.getPassword(), opt.get().getPassword())) {
//			return opt.get();
//		} else {
//			throw new IncorrectPasswordException();
//		}
	}

	// REST End point local
	@PostMapping("/register")
	public ResponseEntity<String> addUser(@RequestBody PixelSenseUser user) {
		// Encoding Password
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
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

	@GetMapping("/getUserDetails")
	public PixelSenseUser getUserDetails() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optUser = userRepository.findById(username);
		if (!optUser.isPresent()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser responseUser = optUser.get();
		responseUser.setLikedMedia(null);
		responseUser.setMediaList(null);
		responseUser.setCommentsOnMedia(null);
		responseUser.setPassword(null);
		responseUser.setLikedMedia(null);
		return responseUser;
	}

	@PostMapping("/updateDesc")
	public ResponseEntity<String> updateProfileDescription(@RequestParam String profileDescription) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userRepository.findById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		user.setProfileDescription(profileDescription);
		userRepository.save(user);
		return new ResponseEntity<String>("description updated", HttpStatus.OK);
	}
	@GetMapping("/getUser/{queryUsername}")
	public PixelSenseUser getUser(@PathVariable String queryUsername) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optUser = userRepository.findById(queryUsername);
		if(!optUser.isPresent()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser responseUser = optUser.get();
		responseUser.setLikedMedia(null);
		responseUser.setMediaList(null);
		responseUser.setCommentsOnMedia(null);
		responseUser.setPassword(null);
		responseUser.setLikedMedia(null);
		return responseUser;
	}
}
