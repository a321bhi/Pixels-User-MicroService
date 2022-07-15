package com.pixels.userservice.exception;

import java.io.Serializable;

public class IncorrectPasswordException extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException() {
		super();
	}

	public IncorrectPasswordException(String message) {
		super(message);
	}

}
