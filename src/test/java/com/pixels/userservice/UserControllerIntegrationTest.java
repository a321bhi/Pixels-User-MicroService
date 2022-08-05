package com.pixels.userservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pixels.userservice.jwt.JwtConfig;
import com.pixels.userservice.model.PixelSenseUser;
import com.pixels.userservice.security.CustomUserDetailsService;
import com.pixels.userservice.service.MediaCommentServiceImpl;
import com.pixels.userservice.service.MediaServiceImpl;
import com.pixels.userservice.service.UserServiceImpl;

@WebMvcTest
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private MediaServiceImpl mediaServiceImpl;

	@MockBean
	private MediaCommentServiceImpl mediaCommentServiceImpl;

	@MockBean
	private WebClient.Builder webClientBuilder;
	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	JwtConfig jwtConfig;

	@MockBean
	CustomUserDetailsService customUserDetailsService;

	@MockBean
	AuthenticationEntryPoint authenticationEntryPoint;

	@Test
	public void shouldGetUserRegisteredAndLogin() throws Exception {

		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		mockUser.setFullName("Mock User");
		mockUser.setEmailAddress("mockuser@example.com");
		mockUser.setPassword("pass");
		mockUser.setDateOfBirth(
				Date.from(LocalDate.now().plusDays(365 * 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		mockUser.setCountryCode("+91");
		mockUser.setPhoneNumber("1122334455");
		when(userServiceImpl.addUser(mockUser)).thenReturn(mockUser);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		mockMvc.perform(
				post("/user/register").contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(mockUser)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(equalTo("mockuser has registered!")));

	}

}
