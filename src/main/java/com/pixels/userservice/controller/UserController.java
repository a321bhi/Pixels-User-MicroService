package com.pixels.userservice.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.pixels.userservice.dto.MediaRequestDTO;
import com.pixels.userservice.dto.PixelsUserDTO;
import com.pixels.userservice.dto.PixelsUserLoginDTO;
import com.pixels.userservice.dto.UserBioDTO;
import com.pixels.userservice.exception.UsernameNotFoundException;
import com.pixels.userservice.jwt.JwtConfig;
import com.pixels.userservice.model.PixelSenseUser;
import com.pixels.userservice.service.UserServiceImpl;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private WebClient.Builder webClientBuilder;
	private String baseUrl = "http://mediaservice";
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	JwtConfig jwtConfig;

	@GetMapping("/check/{username}")
	public Boolean userNameExistsCheck(@PathVariable String username) {
		Optional<PixelSenseUser> opt = userServiceImpl.findUserById(username);
		return opt.isPresent();
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody PixelsUserLoginDTO loginFormModel) {
		String username = loginFormModel.getUsername();
		String password = loginFormModel.getPassword();
		Optional<PixelSenseUser> user = userServiceImpl.findUserById(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException();
		}
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		int numOfDays = 1;
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + (86400*1000*numOfDays);
		Date exp = new Date(expMillis);
		
		String token = Jwts.builder().setSubject(username).claim("authorities", "USER").setIssuedAt(new Date())
				.setExpiration(exp)
				.signWith(jwtConfig.getSecretKeySigned()).compact();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(jwtConfig.getAuthorizationheader(), "Bearer " + token);
		responseHeaders.set("Access-Control-Expose-Headers", jwtConfig.getAuthorizationheader());

		return ResponseEntity.ok().headers(responseHeaders).body("Logged in");
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
		userServiceImpl.addUser(user);
		return new ResponseEntity<>(user.getFirstName() + " data has been added", HttpStatus.OK);
	}

	@DeleteMapping("/{userName}")
	public ResponseEntity<String> deleteUser(@PathVariable String userName) {
		userServiceImpl.deleteUserById(userName);
		return new ResponseEntity<>(userName + " removed successfully", HttpStatus.OK);
	}

	@PatchMapping("/bio")
	public ResponseEntity<String> updateProfileDescription(@RequestBody UserBioDTO bio) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		user.setProfileBio(bio.getBio());
		userServiceImpl.addUser(user);
		return new ResponseEntity<>("description updated", HttpStatus.OK);
	}

	@PatchMapping("/profile")
	public ResponseEntity<String> updateProfile(@RequestBody PixelSenseUser updatedUser) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		user.setEmailAddress(updatedUser.getEmailAddress());
		user.setCountryCode(updatedUser.getCountryCode());
		user.setPhoneNumber(updatedUser.getPhoneNumber());
		user.setFullName(updatedUser.getFullName());
		user.setDateOfBirth(updatedUser.getDateOfBirth());
		userServiceImpl.addUser(user);
		return new ResponseEntity<>("profile updated", HttpStatus.OK);
	}

	@PatchMapping("/password")
	public ResponseEntity<String> updatePassword(@RequestBody PixelsUserLoginDTO loginFormModel) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
		user.setPassword(bCryptPasswordEncoder.encode(loginFormModel.getPassword()));
		userServiceImpl.addUser(user);
		return new ResponseEntity<>("profile updated", HttpStatus.OK);
	}

	@GetMapping("/{queryUsername}")
	public PixelsUserDTO getUser(@PathVariable String queryUsername) {
		Optional<PixelSenseUser> optUser = userServiceImpl.findUserById(queryUsername);
		if (!optUser.isPresent()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser responseUser = optUser.get();
		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();
		MediaRequestDTO profilePic;
		Mono<MediaRequestDTO> profilePicResponse = webClient.get()
				.uri("/service/media/" + responseUser.getProfilePicId()).retrieve().bodyToMono(MediaRequestDTO.class);
		profilePic = profilePicResponse.block();

		PixelsUserDTO userResponsePayload = new PixelsUserDTO(responseUser);
		userResponsePayload.setProfilePicAsBase64(profilePic.getImageAsBase64());
		userResponsePayload.setEmailAddress(responseUser.getEmailAddress());
		userResponsePayload.setCountryCode(responseUser.getCountryCode());
		userResponsePayload.setPhoneNumber(responseUser.getPhoneNumber());
		userResponsePayload.setFollower(responseUser);
		userResponsePayload.setFollowing(responseUser);
		return userResponsePayload;
	}

	@PostMapping("/follow")
	public ResponseEntity<String> followUser(@RequestParam String usernameToFollow) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUserRequester = userServiceImpl.findUserById(username);
		Optional<PixelSenseUser> optionalUserToBeFollowed = userServiceImpl.findUserById(usernameToFollow);
		if (optionalUserRequester.isEmpty() || optionalUserToBeFollowed.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser requestingUser = optionalUserRequester.get();
		PixelSenseUser userToBeFollowed = optionalUserToBeFollowed.get();
		Set<PixelSenseUser> followingSet = requestingUser.getFollowing();
		followingSet.add(userToBeFollowed);
		requestingUser.setFollowing(followingSet);
		userServiceImpl.addUser(requestingUser);
		return new ResponseEntity<>("follow updated", HttpStatus.OK);
	}

	@DeleteMapping("/follow/{usernameToUnfollow}")
	public ResponseEntity<String> unfollowUser(@PathVariable String usernameToUnfollow) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUserRequester = userServiceImpl.findUserById(username);
		Optional<PixelSenseUser> optionalUserToBeFollowed = userServiceImpl.findUserById(usernameToUnfollow);
		if (optionalUserRequester.isEmpty() || optionalUserToBeFollowed.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser requestingUser = optionalUserRequester.get();
		PixelSenseUser userToBeUnfollowed = optionalUserToBeFollowed.get();
		Set<PixelSenseUser> followingSet = requestingUser.getFollowing();
		followingSet.remove(userToBeUnfollowed);
		requestingUser.setFollowing(followingSet);
		userServiceImpl.addUser(requestingUser);
		return new ResponseEntity<>("unfollow success", HttpStatus.OK);
	}
}
