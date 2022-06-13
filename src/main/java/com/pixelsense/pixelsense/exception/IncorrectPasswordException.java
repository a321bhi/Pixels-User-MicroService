package com.pixelsense.pixelsense.exception;

import java.io.Serializable;

public class IncorrectPasswordException extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncorrectPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
