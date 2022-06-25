package com.pixelsense.userservice.exception;

import java.io.Serializable;

public class PostNotFoundException extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PostNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PostNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
