package com.transformuk.hee.tis.tcs.service.exception;

/**
 * Thrown if date values specified in date range for date column filters is not valid.
 */
public class DateRangeColumnFilterException extends RuntimeException {

	public DateRangeColumnFilterException(String message) {
		super(message);
	}
}
