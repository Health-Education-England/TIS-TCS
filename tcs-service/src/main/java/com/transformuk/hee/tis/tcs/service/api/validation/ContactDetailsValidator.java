package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.FieldDiffUtil;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
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
  protected static final String FIELD_NAME_TITLE = "title";
  protected static final String FIELD_NAME_EMAIL = "email";

  // TODO: Better way to validate emails.
  private static final String REGEX_EMAIL = "^$|[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
  private static final String REGEX_EMAIL_ERROR = "Valid email format required.";

  private static final String REGEX_NAME = "^$|^[A-Za-z0-9\\-' ]+";
  private static final String REGEX_NAME_ERROR =
      "No special characters allowed for %s, with the exception of apostrophes, hyphens and spaces.";
  private static final String NULL_NAME_ERROR = "%s is required to create or update the record.";

  private static final String REGEX_PHONE = "^$|^[0-9\\\\+\\- ]+";
  private static final String REGEX_PHONE_ERROR =
      "Only numerical values allowed for %s, no special characters, with the exception of plus, minus and spaces.";

  private static final boolean CURRENT_ONLY = true;

  private final Map<String, Function<ContactDetailsDTO, List<FieldError>>> validators =
      Map.of(
          FIELD_NAME_TITLE, dto -> checkTitle(dto, CURRENT_ONLY),
          FIELD_NAME_EMAIL, this::checkEmail
      );

  private final ReferenceServiceImpl referenceService;
  private final ContactDetailsRepository contactDetailsRepository;

  public ContactDetailsValidator(ReferenceServiceImpl referenceService,
      ContactDetailsRepository contactDetailsRepository) {
    this.referenceService = referenceService;
    this.contactDetailsRepository = contactDetailsRepository;
  }

  /**
   * Custom validation on the ContactDetails DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the title and email address of the person.
   * Validation for update doesn't validate the values which are not changed.
   *
   * @param contactDetailsDto the contactDetails to check
   * @param originalDto       the original contactDetails to update if validation type is Update,
   *                          can be set to null if validation type is Create
   * @param validationType    the validation type
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ContactDetailsDTO contactDetailsDto, ContactDetailsDTO originalDto,
      Class<?> validationType) throws MethodArgumentNotValidException, NoSuchMethodException {

    List<FieldError> fieldErrors = new ArrayList<>();

    if (validationType.equals(Update.class)) {
      Map<String, Object[]> diff = FieldDiffUtil.diff(contactDetailsDto, originalDto);
      validators.forEach((field, validator) -> {
        if (diff.containsKey(field)) {
          fieldErrors.addAll(validator.apply(contactDetailsDto));
        }
      });
    } else {
      validators.values().forEach(validator ->
          fieldErrors.addAll(validator.apply(contactDetailsDto))
      );
    }

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(contactDetailsDto, CONTACT_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);

      Method method = this.getClass()
          .getMethod("validate", ContactDetailsDTO.class,
              ContactDetailsDTO.class, Class.class);
      MethodParameter methodParameter = new MethodParameter(method, 0);
      throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
  }

  private List<FieldError> checkTitle(ContactDetailsDTO contactDetailsDto, boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the grades
    if (StringUtils.isNotEmpty(contactDetailsDto.getTitle())) {
      boolean isExists = referenceService
          .isValueExists(TitleDTO.class, contactDetailsDto.getTitle(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, FIELD_NAME_TITLE,
            String.format("title %s does not exist", contactDetailsDto.getTitle())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkForenames(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("forenames", dto.getForenames(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkSurname(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("surname", dto.getSurname(), fieldErrors, false);
    return fieldErrors;
  }

  private List<FieldError> checkKnownAs(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateName("knownAs", dto.getKnownAs(), fieldErrors, false);
    return fieldErrors;
  }

  private void validateName(String fieldName, String value, List<FieldError> fieldErrors,
      boolean required) {
    if (required && value == null) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName,
          String.format(NULL_NAME_ERROR, fieldName)));
    } else if (value != null && !value.matches(REGEX_NAME)) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName,
          String.format(REGEX_NAME_ERROR, fieldName)));
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
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, fieldName,
          String.format(REGEX_PHONE_ERROR, fieldName)));
    }
  }

  private List<FieldError> checkEmail(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    validateEmailFormat(dto.getEmail(), fieldErrors);
    validateEmailUniqueness(dto, fieldErrors);
    return fieldErrors;
  }

  private void validateEmailFormat(String value, List<FieldError> fieldErrors) {
    if (value != null && !value.matches(REGEX_EMAIL)) {
      fieldErrors.add(
          new FieldError(CONTACT_DETAILS_DTO_NAME, FIELD_NAME_EMAIL, REGEX_EMAIL_ERROR));
    }
  }

  private void validateEmailUniqueness(ContactDetailsDTO dto,
      List<FieldError> fieldErrors) {
    String email = dto.getEmail();
    if (StringUtils.isEmpty(email)) {
      return;
    }
    List<ContactDetails> existingContactDetails = contactDetailsRepository.
        findContactDetailsByEmail(email);
    if (!existingContactDetails.isEmpty() && dto.getId() != null) {
      existingContactDetails.removeIf(cd -> {
        Long id = cd.getId();
        return id.equals((dto.getId()));
      });
    }
    int existingSize = existingContactDetails.size();
    if (existingSize > 0) {
      fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, FIELD_NAME_EMAIL,
          String.format(
              "email %s is not unique, there %s currently %d %s with this email (Person ID: %s)",
              dto.getEmail(), existingSize > 1 ? "are" : "is", existingSize,
              existingSize > 1 ? "persons" : "person",
              existingContactDetails.stream().map(cd -> cd.getId().toString()).collect(
                  Collectors.joining(","))
          )));
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
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkTitle(dto, CURRENT_ONLY));
    fieldErrors.addAll(checkForenames(dto));
    fieldErrors.addAll(checkSurname(dto));
    fieldErrors.addAll(checkKnownAs(dto));
    fieldErrors.addAll(checkTelephoneNumber(dto));
    fieldErrors.addAll(checkMobileNumber(dto));
    fieldErrors.addAll(checkEmail(dto));
    fieldErrors.addAll(checkAddress1(dto));
    fieldErrors.addAll(checkAddress2(dto));
    fieldErrors.addAll(checkPostCode(dto));
    return fieldErrors;
  }
}
