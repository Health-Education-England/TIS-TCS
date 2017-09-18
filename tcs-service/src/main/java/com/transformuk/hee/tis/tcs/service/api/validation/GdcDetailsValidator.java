package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link GdcDetails} that
 * cannot be easily done via annotations
 */
@Component
public class GdcDetailsValidator {

  private static final String GDC_DETAILS_DTO_NAME = "GdcDetailsDTO";


  /**
   * Custom validation on the gdcDetailsDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the gmc status if gdc number is entered.
   *
   * @param gdcDetailsDTO the gdcDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GdcDetailsDTO gdcDetailsDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGdcStatus(gdcDetailsDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gdcDetailsDTO, "GdcDetailsDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkGdcStatus(GdcDetailsDTO gdcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    boolean isGdcNumberPresent = StringUtils.isNotEmpty(gdcDetailsDTO.getGdcNumber());
    boolean isGdcStatusPresent = StringUtils.isNotEmpty(gdcDetailsDTO.getGdcStatus());
    if (gdcDetailsDTO != null) {
      if (isGdcNumberPresent && !isGdcStatusPresent) {
        requireFieldErrors(fieldErrors, "gdcStatus");
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
