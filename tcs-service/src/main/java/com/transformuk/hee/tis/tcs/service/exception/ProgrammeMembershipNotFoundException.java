package com.transformuk.hee.tis.tcs.service.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a programme membership cannot be found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Programme membership not found")
public class ProgrammeMembershipNotFoundException extends RuntimeException {

  public ProgrammeMembershipNotFoundException(UUID uuid) {
    super("Programme membership not found: " + uuid);
  }
}

