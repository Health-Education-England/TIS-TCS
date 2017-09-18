package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link GmcDetails} that
 * cannot be easily done via annotations
 */
@Component
public class GmcDetailsValidator {

  private static final String GMC_DETAILS_DTO_NAME = "GmcDetailsDTO";


  /**
   * Custom validation on the gmcDetailsDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the gmc status if gmc number is entered.
   *
   * @param gmcDetailsDTO the gmcDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GmcDetailsDTO gmcDetailsDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGmcStatus(gmcDetailsDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gmcDetailsDTO, "GmcDetailsDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkGmcStatus(GmcDetailsDTO gmcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    boolean isGmcNumberPresent = StringUtils.isNotEmpty(gmcDetailsDTO.getGmcNumber());
    boolean isGmcStatusPresent = StringUtils.isNotEmpty(gmcDetailsDTO.getGmcStatus());
    if (gmcDetailsDTO != null) {
      if (isGmcNumberPresent && !isGmcStatusPresent) {
        requireFieldErrors(fieldErrors, "gmcStatus");
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
