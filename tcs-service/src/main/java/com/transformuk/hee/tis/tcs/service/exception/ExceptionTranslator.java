package com.transformuk.hee.tis.tcs.service.exception;

import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

  private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

  @ExceptionHandler(ConcurrencyFailureException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public ErrorVM processConcurrencyError(ConcurrencyFailureException ex) {
    log.error(ex.getMessage(), ex);
    return new ErrorVM(ErrorConstants.ERR_CONCURRENCY_FAILURE,
        "You are acting on stale data, please refresh");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorVM processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return processFieldErrors(fieldErrors);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorVM processValidationError(ValidationException ex) {
    log.error(ex.getMessage(), ex);
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return processFieldErrors(fieldErrors);
  }

  /**
   * This exception occurs if we have an enum in a DTO such as {@link
   * com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO#status} and the REST request coming in does
   * not provide the proper ENUM value.
   *
   * @param ex the exception to intercept
   * @return the error object to return to the user
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorVM processBadEnumError(HttpMessageNotReadableException ex) {
    log.error(ex.getMessage(), ex);
    ErrorVM dto = new ErrorVM(ErrorConstants.ERR_VALIDATION);
    dto.add(null, null, ex.getMessage());
    return dto;
  }

  @ExceptionHandler(CustomParameterizedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ParameterizedErrorVM processParameterizedValidationError(CustomParameterizedException ex) {
    log.error(ex.getMessage(), ex);
    return ex.getErrorVM();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ErrorVM processAccessDeniedException(AccessDeniedException e) {
    log.error(e.getMessage(), e);
    return new ErrorVM(ErrorConstants.ERR_ACCESS_DENIED, e.getMessage());
  }

  private ErrorVM processFieldErrors(List<FieldError> fieldErrors) {
    ErrorVM dto = new ErrorVM(ErrorConstants.ERR_VALIDATION);

    for (FieldError fieldError : fieldErrors) {
      dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
    }

    return dto;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorVM processMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    log.error(exception.getMessage(), exception);
    return new ErrorVM(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, exception.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorVM> processIllegalArgumentException(Exception ex) {
    log.error(ex.getMessage(), ex);
    BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
    ErrorVM errorVM = new ErrorVM("Bad request", ex.getMessage());
    return builder.body(errorVM);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorVM> processRuntimeException(Exception ex) {
    BodyBuilder builder;
    ErrorVM errorVM;
    log.error(ex.getMessage(), ex);
    ResponseStatus responseStatus = AnnotationUtils
        .findAnnotation(ex.getClass(), ResponseStatus.class);
    if (responseStatus != null) {
      builder = ResponseEntity.status(responseStatus.value());
      errorVM = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason());
    } else {
      builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
      errorVM = new ErrorVM(ErrorConstants.ERR_INTERNAL_SERVER_ERROR, "Internal server error");
    }
    return builder.body(errorVM);
  }

  @ExceptionHandler(NationalPostNumberRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorVM processNationalPostNumberException(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(fieldErrors);
  }

  /**
   * Handler for an exception thrown at the service level, that checks if the current user is
   * authorised to do a certain action
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(AccessUnauthorisedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ErrorVM accessUnauthorisedException(AccessUnauthorisedException ex) {
    return new ErrorVM(ErrorConstants.ERR_ACCESS_DENIED, ex.getMessage());
  }
}
