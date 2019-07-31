package com.transformuk.hee.tis.tcs.service.exception;

/**
 * Exception to represent when a user is attempting to access a resource record that the user does
 * not have access to
 */
public class AccessUnauthorisedException extends RuntimeException {

  public AccessUnauthorisedException(String message) {
    super(message);
  }
}
