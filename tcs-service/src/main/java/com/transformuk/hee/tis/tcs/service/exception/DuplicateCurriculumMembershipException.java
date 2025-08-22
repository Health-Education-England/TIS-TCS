package com.transformuk.hee.tis.tcs.service.exception;

/**
 * Exception thrown to indicate that an attempt was made to create a duplicate
 * curriculum membership.
 *
 * <p>This is an unchecked exception extending {@link RuntimeException}.</p>
 */
public class DuplicateCurriculumMembershipException extends RuntimeException {
  public DuplicateCurriculumMembershipException(String message) {
    super(message);
  }
}
