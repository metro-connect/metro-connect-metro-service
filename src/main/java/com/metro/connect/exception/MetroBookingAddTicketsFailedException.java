package com.metro.connect.exception;

public class MetroBookingAddTicketsFailedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetroBookingAddTicketsFailedException() {
		super("Failed to add Scheduled Metro Tickets in Booking Table");
	}

	public MetroBookingAddTicketsFailedException(String message) {
		super(message);
	}

}
