package com.pixels.userservice.exception;

import java.io.Serializable;

public class PostNotFoundException extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PostNotFoundException() {
		super();
	}

	public PostNotFoundException(String message) {
		super(message);
	}

}
