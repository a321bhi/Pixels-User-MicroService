package com.pixelsense.pixelsense.exception;

import java.io.Serializable;

public class UserNameNotFoundException  extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNameNotFoundException() {
		super();
	}

	public UserNameNotFoundException(String message) {
		super(message);
	}

}
