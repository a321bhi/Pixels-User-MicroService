package com.pixels.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.pixels.userservice.dto.PixelsUserLoginDTO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PixelsLiveTest {

	private static final String API_ROOT = "http://localhost:8090/user";

//	private PixelSenseUser createRandomUser() {
//		PixelSenseUser user = new PixelSenseUser();
//		user.setUsername(RandomStringUtils.randomAlphabetic(10));
//		user.setPassword(RandomStringUtils.randomAlphabetic(10));
//		user.setFullName(RandomStringUtils.randomAlphabetic(10));
//		user.setFirstName(RandomStringUtils.randomAlphabetic(10));
//		user.setLastName(RandomStringUtils.randomAlphabetic(10));
//		user.setMiddleName(RandomStringUtils.randomAlphabetic(10));
//		user.setGender("male");
//		user.setEmailAddress(user.getUserName() + "@example.com");
//		user.setDateOfBirth(Date.from(Instant.now().minus(Duration.ofDays(20 * 365))));
//		user.setCountryCode("+91");
//		user.setPhoneNumber("1234567890");
//		return user;
//	}

	private String createUserAsUri() {
		Response response = RestAssured.given().contentType(ContentType.JSON).when()
				.body(new HashMap<String, String>() {
					{
						put("fullName", "Test User");
						put("emailAddress", "test@example.com");
						put("username", "testuser");
						put("countryCode", "+91");
						put("phoneNumber", "1122334455");
						put("dateOfBirth", "1995-04-21");
						put("password", "root");
					}
				}).post(API_ROOT + "/register");
		return API_ROOT + "/" + "testuser";
	}

	@Test
	public void whenGetCreatedUserByUsername_thenOK() {
//		PixelSenseUser user = createRandomUser();

		String location = createUserAsUri();
		PixelsUserLoginDTO pixelsUserLoginDTO = new PixelsUserLoginDTO();
		pixelsUserLoginDTO.setUsername("testuser");
		pixelsUserLoginDTO.setPassword("root");
		Response response2 = RestAssured.given().contentType(ContentType.JSON).body(pixelsUserLoginDTO)
				.post(API_ROOT + "/login");
		String token = response2.getHeader("Authorization");
		Header header = new Header("Authorization", token);
		Response response = RestAssured.given().header(header).get(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals("Test User", response.jsonPath().get("fullName"));
		assertEquals("test@example.com", response.jsonPath().get("emailAddress"));
	}

}