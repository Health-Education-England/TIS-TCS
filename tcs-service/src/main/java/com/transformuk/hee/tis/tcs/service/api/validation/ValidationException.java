package com.transformuk.hee.tis.tcs.service.api.validation;

import org.springframework.validation.BindingResult;

public class ValidationException extends Exception {

  private BindingResult bindingResult;

  public ValidationException(BindingResult bindingResult) {
    this.bindingResult = bindingResult;
  }

  public BindingResult getBindingResult() {
    return bindingResult;
  }
}
