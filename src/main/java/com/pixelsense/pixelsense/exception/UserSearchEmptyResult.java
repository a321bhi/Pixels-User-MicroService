package com.pixelsense.pixelsense.exception;
import java.io.Serializable;

public class UserSearchEmptyResult extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSearchEmptyResult() {
		super();
	}

	public UserSearchEmptyResult(String message) {
		super(message);
	}

}
