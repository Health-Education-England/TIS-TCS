package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.EeaResident;
import com.transformuk.hee.tis.tcs.api.enumeration.Settled;
import com.transformuk.hee.tis.tcs.service.api.util.FieldDiffUtil;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

  protected static final String FIELD_NAME_PERMIT_TO_WORK = "permitToWork";
  protected static final String FIELD_NAME_VISA_ISSUED = "visaIssued";
  protected static final String FIELD_NAME_VISA_VALID_TO = "visaValidTo";
  private static final String DTO_NAME = "RightToWorkDTO";
  private final ReferenceService referenceService;
  private final PersonRepository personRepository;

  RightToWorkValidator(ReferenceService referenceService, PersonRepository personRepository) {
    this.referenceService = referenceService;
    this.personRepository = personRepository;
  }

  /**
   * Custom validation on the rightToWork DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the permit to work, visa status and EEA residency.
   * Validation for update doesn't validate the values which are not changed.
   *
   * @param rightToWorkDto the rightToWork to check
   * @param originalDto    the original rightToWork to update if validation type is Update,
   *                       can be set to null if validation type is Create
   * @param validationType the validation type
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(RightToWorkDTO rightToWorkDto, RightToWorkDTO originalDto,
      Class<?> validationType)
      throws MethodArgumentNotValidException {
    if (rightToWorkDto == null) {
      return;
    }

    List<FieldError> fieldErrors = new ArrayList<>();

    Set<String> visaDateFields = Set.of(FIELD_NAME_VISA_ISSUED, FIELD_NAME_VISA_VALID_TO);

    if (validationType.equals(Update.class)) {
      Map<String, Object[]> diff = FieldDiffUtil.diff(rightToWorkDto, originalDto);

      if (diff.keySet().stream().anyMatch(visaDateFields::contains)) {
        checkVisaDates(rightToWorkDto, fieldErrors, false);
      }
      if (diff.containsKey(FIELD_NAME_PERMIT_TO_WORK)) {
        checkPermitToWork(rightToWorkDto, fieldErrors);
      }
    } else {
      checkPermitToWork(rightToWorkDto, fieldErrors);
      checkVisaDates(rightToWorkDto, fieldErrors, false);
    }

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(rightToWorkDto,
          DTO_NAME);
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

  private void checkVisaDates(RightToWorkDTO dto, List<FieldError> fieldErrors,
      boolean checkExistingDbValues) {

    if (dto != null) {
      LocalDate visaIssued = dto.getVisaIssued();
      LocalDate visaValidTo = dto.getVisaValidTo();

      if (visaIssued == null && visaValidTo == null) {
        //Nothing to check
        return;
      }

      if (visaIssued != null && visaValidTo != null) {
        //only check the values passed in
        if (visaIssued.isAfter(visaValidTo)) {
          FieldError fieldError =
              new FieldError(DTO_NAME, FIELD_NAME_VISA_ISSUED, "visaIssued must be"
                  + " before visaValidTo.");
          fieldErrors.add(fieldError);
        }
      } else if (checkExistingDbValues) {
        checkDbVisaDates(fieldErrors, dto.getId(), visaIssued, visaValidTo);
      }
    }
  }

  private void checkDbVisaDates(List<FieldError> fieldErrors, Long personId,
      LocalDate visaIssued, LocalDate visaValidTo) {

    if (personId == null) {
      return;
    }

    Optional<Person> originalPersonRecord = personRepository.findPersonById(personId);

    if (originalPersonRecord.isPresent()) {
      RightToWork oldRtwDto = originalPersonRecord.get().getRightToWork();
      if (oldRtwDto == null) {
        return;
      }
      LocalDate oldVisaIssued = oldRtwDto.getVisaIssued();
      LocalDate oldVisaValidTo = oldRtwDto.getVisaValidTo();

      if (visaIssued != null && oldVisaValidTo != null && visaIssued.isAfter(oldVisaValidTo)) {
        FieldError fieldError =
            new FieldError(DTO_NAME, FIELD_NAME_VISA_ISSUED, "Visa Issued Date "
                + "conflicts with Visa Valid to date already in Database");
        fieldErrors.add(fieldError);
      } else if (visaValidTo != null && oldVisaIssued != null
          && oldVisaIssued.isAfter(visaValidTo)) {
        FieldError fieldError =
            new FieldError(DTO_NAME, FIELD_NAME_VISA_VALID_TO, "Visa Valid To Date "
                + "conflicts with Visa Issued date already in Database");
        fieldErrors.add(fieldError);
      }
    }
  }

  private void checkPermitToWork(RightToWorkDTO dto, List<FieldError> fieldErrors) {
    String permitToWork = dto.getPermitToWork();

    if (permitToWork != null) {
      boolean exists = referenceService.isValueExists(PermitToWorkDTO.class, permitToWork, true);

      if (!exists) {
        FieldError fieldError = new FieldError(DTO_NAME, FIELD_NAME_PERMIT_TO_WORK,
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
  public List<FieldError> validateForBulk(RightToWorkDTO rightToWorkDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (rightToWorkDto != null) {
      checkEeaResident(rightToWorkDto, fieldErrors);
      checkSettled(rightToWorkDto, fieldErrors);
      checkVisaDates(rightToWorkDto, fieldErrors, true);
      checkPermitToWork(rightToWorkDto, fieldErrors);
    }
    return fieldErrors;
  }
}
