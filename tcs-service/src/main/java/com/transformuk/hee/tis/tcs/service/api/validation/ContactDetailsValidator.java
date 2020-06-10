package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link ContactDetails} that cannot be easily done via
 * annotations
 */
@Component
public class ContactDetailsValidator {

  private static final String CONTACT_DETAILS_DTO_NAME = "ContactDetailsDTO";

  // TODO: Better way to validate emails.
  private static final String REGEX_EMAIL = "^$|^[A-Za-z0-9+_.-]+@(.+)$";
  private static final String REGEX_EMAIL_ERROR = "Valid email format required.";


  private static final String REGEX_NAME = "^$|^[A-Za-z0-9\\-\\\\' ]+";
  private static final String REGEX_NAME_ERROR =
      "No special characters, with the exception of apostrophes, hyphens and spaces.";
  private static final String NULL_NAME_ERROR = "%s is required to create or update the record.";

  private static final String REGEX_PHONE = "^$|^[0-9\\\\+\\- ]+";
  private static final String REGEX_PHONE_ERROR =
      "Only numerical values allowed for TelephoneNumber, no special characters, with the exception of plus, minus and spaces.";

  private final ReferenceServiceImpl referenceService;

  public ContactDetailsValidator(ReferenceServiceImpl referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the gmcDetailsDTO DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the gmc status if gmc number is entered.
   *
   * @param dto the contactDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ContactDetailsDTO dto) throws MethodArgumentNotValidException {
    final boolean currentOnly = false;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkTitle(dto, currentOnly));
    fieldErrors.addAll(checkAddress1(dto));
    fieldErrors.addAll(checkAddress2(dto));
    fieldErrors.addAll(checkPostCode(dto));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(dto, CONTACT_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkTitle(ContactDetailsDTO contactDetailsDto, boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the grades
    if (StringUtils.isNotEmpty(contactDetailsDto.getTitle())) {
      Boolean isExists = referenceService
          .isValueExists(TitleDTO.class, contactDetailsDto.getTitle(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, "title",
            String.format("title %s does not exist", contactDetailsDto.getTitle())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkForenames(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("forenames", dto.getForenames(), fieldErrors, true);
    return fieldErrors;
  }

  private List<FieldError> checkLegalForenames(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("legalForenames", dto.getLegalForenames(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkSurname(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("surname", dto.getSurname(), fieldErrors, true);
    return fieldErrors;
  }

  private List<FieldError> checkLegalSurname(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("legalSurname", dto.getLegalSurname(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkKnownAs(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("knownAs", dto.getKnownAs(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkMaidenName(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("maidenName", dto.getMaidenName(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkInitials(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("initials", dto.getInitials(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkCountry(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("country", dto.getCountry(), fieldErrors, false);
    return fieldErrors;
  }

  private void validateName(String fieldName, String value, List<FieldError> fieldErrors,
      boolean required) {
    if (required && value == null) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName,
          String.format(NULL_NAME_ERROR, fieldName)));
    } else if (value != null && !value.matches(REGEX_NAME)) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName, REGEX_NAME_ERROR));
    }
  }

  private List<FieldError> checkMobileNumber(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validatePhoneNumber("mobileNumber", dto.getMobileNumber(), fieldErrors);
    return fieldErrors;
  }

  private List<FieldError> checkTelephoneNumber(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validatePhoneNumber("telephoneNumber", dto.getTelephoneNumber(), fieldErrors);
    return fieldErrors;
  }

  private void validatePhoneNumber(String fieldName, String value, List<FieldError> fieldErrors) {
    if (value != null && !value.matches(REGEX_PHONE)) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName, REGEX_PHONE_ERROR));
    }
  }

  private List<FieldError> checkEmail(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateEmail("email", dto.getEmail(), fieldErrors);
    return fieldErrors;
  }

  private List<FieldError> checkWorkEmail(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateEmail("workEmail", dto.getWorkEmail(), fieldErrors);
    return fieldErrors;
  }

  private void validateEmail(String fieldName, String value, List<FieldError> fieldErrors) {
    if (value != null && !value.matches(REGEX_EMAIL)) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName, REGEX_EMAIL_ERROR));
    }
  }

  private List<FieldError> checkAddress1(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    final String field_address1 = "address1";

    if (StringUtils.isEmpty(dto.getAddress1())) {
      if (StringUtils.isNotEmpty(dto.getAddress2())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, field_address1,
            "address1 is required when address2 is populated.");
        fieldErrors.add(fieldError);
      }

      if (StringUtils.isNotEmpty(dto.getAddress3())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, field_address1,
            "address1 is required when address3 is populated.");
        fieldErrors.add(fieldError);
      }

      if (StringUtils.isNotEmpty(dto.getPostCode())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, field_address1,
            "address1 is required when postCode is populated.");
        fieldErrors.add(fieldError);
      }
    }

    return fieldErrors;
  }

  private List<FieldError> checkAddress2(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (StringUtils.isEmpty(dto.getAddress2()) && StringUtils.isNotEmpty(dto.getAddress3())) {
      FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, "address2",
          "address2 is required when address3 is populated.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  private List<FieldError> checkPostCode(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (StringUtils.isEmpty(dto.getPostCode()) && (StringUtils.isNotEmpty(dto.getAddress1())
        || StringUtils.isNotEmpty(dto.getAddress2()) || StringUtils
        .isNotEmpty(dto.getAddress3()))) {
      FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, "postCode",
          "postCode is required when address is populated.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  /**
   * Custom validation on the ContactDetailsDTO for bulk upload.
   *
   * @param dto the ContactDetailsDTO to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(ContactDetailsDTO dto) {
    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkTitle(dto, currentOnly));
    fieldErrors.addAll(checkForenames(dto));
    fieldErrors.addAll(checkLegalForenames(dto));
    fieldErrors.addAll(checkSurname(dto));
    fieldErrors.addAll(checkLegalSurname(dto));
    fieldErrors.addAll(checkKnownAs(dto));
    fieldErrors.addAll(checkMaidenName(dto));
    fieldErrors.addAll(checkInitials(dto));
    fieldErrors.addAll(checkCountry(dto));
    fieldErrors.addAll(checkTelephoneNumber(dto));
    fieldErrors.addAll(checkMobileNumber(dto));
    fieldErrors.addAll(checkEmail(dto));
    fieldErrors.addAll(checkWorkEmail(dto));
    fieldErrors.addAll(checkAddress1(dto));
    fieldErrors.addAll(checkAddress2(dto));
    fieldErrors.addAll(checkPostCode(dto));
    return fieldErrors;
  }
}
