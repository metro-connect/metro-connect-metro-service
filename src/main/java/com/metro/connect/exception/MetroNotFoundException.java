package com.metro.connect.exception;

public class MetroNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetroNotFoundException() {
		super("Metro not found in database");
	}

	public MetroNotFoundException(String message) {
		super(message);
	}

}
