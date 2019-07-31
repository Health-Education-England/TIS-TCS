package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link RightToWork} that cannot be easily done via
 * annotations
 */
@Component
public class RightToWorkValidator {

  private static final String NO = "NO";
  private static final String RIGHT_TO_WORK_DTO = "RightToWorkDTO";

  /**
   * Custom validation on the rightToWork DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the permit to work, visa status and EEA residency.
   *
   * @param rightToWorkDTO the rightToWork to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(RightToWorkDTO rightToWorkDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(rightToWorkDTO,
          "RightToWorkDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private boolean isEeaResident(RightToWorkDTO rightToWorkDTO) {
    return !(StringUtils.isEmpty(rightToWorkDTO.getEeaResident()) || NO
        .equalsIgnoreCase(rightToWorkDTO.getEeaResident()));
  }

  private boolean isSettled(RightToWorkDTO rightToWorkDTO) {
    return !(StringUtils.isEmpty(rightToWorkDTO.getSettled()) || NO
        .equalsIgnoreCase(rightToWorkDTO.getSettled()));
  }


}
