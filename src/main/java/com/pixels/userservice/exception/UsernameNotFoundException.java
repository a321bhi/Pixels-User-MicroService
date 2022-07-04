package com.pixels.userservice.exception;

import java.io.Serializable;

public class UsernameNotFoundException  extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameNotFoundException() {
		super();
	}

	public UsernameNotFoundException(String message) {
		super(message);
	}

}
