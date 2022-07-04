package com.pixels.userservice.exception;

import java.io.Serializable;

public class CommentNotFound extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentNotFound() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommentNotFound(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
