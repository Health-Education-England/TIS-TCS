package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link RightToWork} that
 * cannot be easily done via annotations
 */
@Component
public class RightToWorkValidator {

  private static final String NO = "NO";
  private static final String RIGHT_TO_WORK_DTO = "RightToWorkDTO";

  /**
   * Custom validation on the rightToWork DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the permit to work, visa status and EEA residency.
   *
   * @param rightToWorkDTO the rightToWork to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(RightToWorkDTO rightToWorkDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
//    fieldErrors.addAll(checkPermitToWork(rightToWorkDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(rightToWorkDTO, "RightToWorkDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkPermitToWork(RightToWorkDTO rightToWorkDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (rightToWorkDTO != null) {
      boolean isEeaResident = isEeaResident(rightToWorkDTO);
      boolean isSettled = isSettled(rightToWorkDTO);
      boolean isVisaIssued = (rightToWorkDTO.getVisaIssued() == null ? false : true);
      boolean isVisaValidTo = (rightToWorkDTO.getVisaValidTo() == null ? false : true);
      boolean isVisaDetails = (rightToWorkDTO.getVisaDetails() == null ? false : true);

      if (!isEeaResident && !isSettled) {
        if (!isVisaIssued) {
          requireFieldErrors(fieldErrors, "visaIssued");
        }
        if (!isVisaValidTo) {
          requireFieldErrors(fieldErrors, "visaValidTo");
        }
        if (!isVisaDetails) {
          requireFieldErrors(fieldErrors, "visaDetails");
        }
        else{
          requireFieldErrors(fieldErrors, "settled");
        }
      }

    }
    return fieldErrors;
  }

  private boolean isEeaResident(RightToWorkDTO rightToWorkDTO) {
    return !(StringUtils.isEmpty(rightToWorkDTO.getEeaResident()) || NO.equalsIgnoreCase(rightToWorkDTO.getEeaResident()));
  }

  private boolean isSettled(RightToWorkDTO rightToWorkDTO) {
    return !(StringUtils.isEmpty(rightToWorkDTO.getSettled()) || NO.equalsIgnoreCase(rightToWorkDTO.getSettled()));
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(RIGHT_TO_WORK_DTO, field,
        String.format("%s is required", field)));
  }

}
