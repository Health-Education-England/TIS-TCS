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
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";
  private ReferenceServiceImpl referenceService;

  public ContactDetailsValidator(ReferenceServiceImpl referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the gmcDetailsDTO DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the gmc status if gmc number is entered.
   *
   * @param contactDetailsDTO the contactDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ContactDetailsDTO contactDetailsDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkTitle(contactDetailsDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(contactDetailsDTO,
          "ContactDetailsDTO");
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


}
