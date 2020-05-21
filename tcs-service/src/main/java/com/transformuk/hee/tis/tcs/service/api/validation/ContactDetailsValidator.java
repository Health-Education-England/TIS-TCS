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
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkTitle(dto));
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

  private List<FieldError> checkTitle(ContactDetailsDTO contactDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the grades
    if (StringUtils.isNotEmpty(contactDetailsDTO.getTitle())) {
      Boolean isExists = referenceService
          .isValueExists(TitleDTO.class, contactDetailsDTO.getTitle());
      if (!isExists) {
        fieldErrors.add(new FieldError(CONTACT_DETAILS_DTO_NAME, "title",
            String.format("title %s does not exist", contactDetailsDTO.getTitle())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkAddress1(ContactDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (StringUtils.isEmpty(dto.getAddress1())) {
      if (StringUtils.isNotEmpty(dto.getAddress2())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, "address1",
            "address1 is required when address2 is populated.");
        fieldErrors.add(fieldError);
      }

      if (StringUtils.isNotEmpty(dto.getAddress3())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, "address1",
            "address1 is required when address3 is populated.");
        fieldErrors.add(fieldError);
      }

      if (StringUtils.isNotEmpty(dto.getPostCode())) {
        FieldError fieldError = new FieldError(CONTACT_DETAILS_DTO_NAME, "address1",
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
}
