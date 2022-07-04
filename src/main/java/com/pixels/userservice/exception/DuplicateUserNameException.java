package com.pixels.userservice.exception;

import java.io.Serializable;

public class DuplicateUserNameException extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateUserNameException() {
		super();
	}

	public DuplicateUserNameException(String message) {
		super(message);
	}

}
