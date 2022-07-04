package com.pixels.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { DuplicateUserNameException.class })
	public ResponseEntity<String> duplicateUserNameExceptionHandler() {
		return new ResponseEntity<String>("Username already exists..", HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = { UsernameNotFoundException.class })
	public ResponseEntity<String> userNameNotFoundExceptionHandler() {
		return new ResponseEntity<String>("User not found or does not exist..", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { UserSearchEmptyResult.class })
	public ResponseEntity<String> userSearchEmptyResultHandler() {
		return new ResponseEntity<String>("No users found..", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { IncorrectPasswordException.class })
	public ResponseEntity<String> incorrectPasswordExceptionHandler() {
		return new ResponseEntity<String>("Incorrect password..", HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = { PostNotFoundException.class })
	public ResponseEntity<String> postNotFoundExceptionHandler() {
		return new ResponseEntity<String>("Post not found..", HttpStatus.NOT_FOUND);
	}
	

	@ExceptionHandler(value = { CommentNotFound.class })
	public ResponseEntity<String> commentNotFoundHandler() {
		return new ResponseEntity<String>("Comment not found..", HttpStatus.NOT_FOUND);
	}
}
