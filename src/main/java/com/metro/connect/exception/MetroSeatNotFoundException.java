package com.metro.connect.exception;

public class MetroSeatNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetroSeatNotFoundException() {
		super("Seat not found in database");
	}

	public MetroSeatNotFoundException(String message) {
		super(message);
	}

}
