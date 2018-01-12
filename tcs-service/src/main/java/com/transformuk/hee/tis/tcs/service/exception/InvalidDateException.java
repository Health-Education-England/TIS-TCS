package com.transformuk.hee.tis.tcs.service.exception;

/**
 * Thrown if date values specified in date range for date column filters is not valid.
 */
public class InvalidDateException extends RuntimeException {

	public InvalidDateException(String message) {
		super(message);
	}
}
