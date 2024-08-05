package com.metro.connect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metro.connect.dto.CommonApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(MetroNotFoundException.class)
	public ResponseEntity<CommonApiResponse> handleUserNotFoundException(MetroNotFoundException ex) {
		String responseMessage = ex.getMessage();
		
		CommonApiResponse apiResponse = CommonApiResponse.builder().responseMessage(responseMessage).isSuccess(false).status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MetroBookingAddTicketsFailedException.class)
	public ResponseEntity<CommonApiResponse> handleMetroBookingAddTicketsFailedException(MetroBookingAddTicketsFailedException ex) {
		String responseMessage = ex.getMessage();
		
		CommonApiResponse apiResponse = CommonApiResponse.builder().responseMessage(responseMessage).isSuccess(false).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

}
