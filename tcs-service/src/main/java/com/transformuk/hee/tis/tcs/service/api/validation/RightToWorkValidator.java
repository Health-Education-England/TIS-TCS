package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.EeaResident;
import com.transformuk.hee.tis.tcs.api.enumeration.Settled;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Holds more complex custom validation for a {@link RightToWork} that cannot be easily done via
 * annotations.
 */
@Component
public class RightToWorkValidator {

  private static final String DTO_NAME = "RightToWorkDTO";
  private static String field = "visaIssued";

  private final ReferenceService referenceService;
  private final PersonRepository personRepository;

  RightToWorkValidator(ReferenceService referenceService, PersonRepository personRepository) {
    this.referenceService = referenceService;
    this.personRepository = personRepository;
  }

  /**
   * Custom validation on the rightToWork DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the permit to work, visa status and EEA residency.
   *
   * @param dto the rightToWork to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(RightToWorkDTO dto) throws MethodArgumentNotValidException {
    if (dto == null) {
      return;
    }

    List<FieldError> fieldErrors = new ArrayList<>();
    checkPermitToWork(dto, fieldErrors);

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

  private void checkVisaDates(List<FieldError> fieldErrors, RightToWorkDTO dto, Long personId) {

      LocalDate visaIssued = dto.getVisaIssued();
      LocalDate visaValidTo = dto.getVisaValidTo();
      Optional<Person> originalPersonRecord = personRepository.findPersonById(personId);

      if (visaIssued != null && visaValidTo != null && visaIssued.isAfter(visaValidTo)) {
        FieldError fieldError =
            new FieldError(DTO_NAME, field, "visaIssued must be before "
                + "visaValidTo.");
        fieldErrors.add(fieldError);
      } else if (originalPersonRecord.isPresent()) {
        Person existingPerson = originalPersonRecord.get();
        RightToWork oldRtwDto = existingPerson.getRightToWork();
        if (visaIssued != null && visaValidTo == null) {
          if (visaIssued.isAfter(oldRtwDto.getVisaValidTo())) {
            FieldError fieldError =
                new FieldError(DTO_NAME, field, "visaIssued is after "
                    + "current visaValidTo date.");
            fieldErrors.add(fieldError);
          }
        } else if (visaValidTo != null && visaIssued == null
            && visaValidTo.isBefore(oldRtwDto.getVisaIssued())) {
          FieldError fieldError =
              new FieldError(DTO_NAME, field, "visaValidTo date is "
                  + "before current visaIssued date.");
          fieldErrors.add(fieldError);
        }
      }
    }

  private void checkPermitToWork(RightToWorkDTO dto, List<FieldError> fieldErrors) {
    String permitToWork = dto.getPermitToWork();

    if (permitToWork != null) {
      boolean exists = referenceService.isValueExists(PermitToWorkDTO.class, permitToWork, true);

      if (!exists) {
        FieldError fieldError = new FieldError(DTO_NAME, "permitToWork",
            "permitToWork must match a current reference value.");
        fieldErrors.add(fieldError);
      }
    }
  }

  /**
   * Custom validation on the RightToWorkDTO for bulk upload.
   *
   * @param rightToWorkDto the rightToWorkDto to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(RightToWorkDTO rightToWorkDto, Long personId) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (rightToWorkDto != null) {
      checkEeaResident(rightToWorkDto, fieldErrors);
      checkSettled(rightToWorkDto, fieldErrors);
      checkVisaDates(fieldErrors, rightToWorkDto, personId);
      checkPermitToWork(rightToWorkDto, fieldErrors);
    }
    return fieldErrors;
  }
}
