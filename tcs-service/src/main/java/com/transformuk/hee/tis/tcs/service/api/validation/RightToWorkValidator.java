package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.EeaResident;
import com.transformuk.hee.tis.tcs.api.enumeration.Settled;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link RightToWork} that cannot be easily done via
 * annotations.
 */
@Component
public class RightToWorkValidator {

  private static final String DTO_NAME = "RightToWorkDTO";

  /**
   * Custom validation on the rightToWork DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the permit to work, visa status and EEA residency.
   *
   * @param dto the rightToWork to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(RightToWorkDTO dto) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    checkEeaResident(dto, fieldErrors);
    checkSettled(dto, fieldErrors);
    checkVisaDates(dto, fieldErrors);

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);

      Method method = this.getClass().getMethods()[0];
      MethodParameter methodParameter = new MethodParameter(method, 0);
      throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
  }

  private void checkEeaResident(RightToWorkDTO dto, List<FieldError> fieldErrors) {
    String eeaResident = dto.getEeaResident();

    if (eeaResident != null) {
      boolean valid =
          Arrays.stream(EeaResident.values()).map(Enum::name).anyMatch(n -> n.equals(eeaResident));

      if (!valid) {
        FieldError fieldError =
            new FieldError(DTO_NAME, "eeaResident", "eeaResident must match a reference value.");
        fieldErrors.add(fieldError);
      }
    }
  }

  private void checkSettled(RightToWorkDTO dto, List<FieldError> fieldErrors) {
    String settled = dto.getSettled();

    if (settled != null && Arrays.stream(Settled.values()).map(Enum::name)
        .noneMatch(n -> n.equals(settled))) {
      FieldError fieldError =
          new FieldError(DTO_NAME, "settled", "settled must match a reference value.");
      fieldErrors.add(fieldError);
    }
  }

  private void checkVisaDates(RightToWorkDTO dto, List<FieldError> fieldErrors) {
    LocalDate visaIssued = dto.getVisaIssued();
    LocalDate visaValidTo = dto.getVisaValidTo();

    if (visaIssued != null && visaValidTo != null && visaIssued.isAfter(visaValidTo)) {
      FieldError fieldError =
          new FieldError(DTO_NAME, "visaIssued", "visaIssued must be before visaValidTo.");
      fieldErrors.add(fieldError);
    }
  }

  /**
   * Custom validation on the RightToWorkDTO for bulk upload.
   *
   * @param rightToWorkDto the rightToWorkDto to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(RightToWorkDTO rightToWorkDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    checkEeaResident(rightToWorkDto, fieldErrors);
    checkSettled(rightToWorkDto, fieldErrors);
    checkVisaDates(rightToWorkDto, fieldErrors);
    return fieldErrors;
  }
}
