package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link Person} that cannot be easily done via
 * annotations
 */
@Component
public class PersonValidator {

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonValidator.class);

  private static final String PERSON_DTO_NAME = "PersonDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";
  private PersonRepository personRepository;

  public PersonValidator(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   * Custom validation on the personDto DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the public health number unique check if it is
   * entered.
   *
   * @param personDto the person to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(PersonDTO personDto) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPublicHealthNumber(personDto));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(personDto,
          PERSON_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      Method method = this.getClass().getMethods()[0];
      throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
    }
  }

  /**
   * Custom validation on {@link PersonDTO} for bulk upload, this is meant to supplement the
   * annotation based validation already in place. Validation errors are added to the DTO's message
   * list.
   *
   * @param personDtos The persons to check.
   */
  public void validateForBulk(List<PersonDTO> personDtos) {
    for (PersonDTO personDto : personDtos) {
      List<ObjectError> errors = validateForBulk(personDto);

      if (errors.isEmpty()) {
        continue;
      }

      LOGGER.debug("Validation errors occurred for Person: {}", personDto);

      for (ObjectError error : errors) {
        String errorMessage = error.getDefaultMessage();
        personDto.addMessage(errorMessage);

        LOGGER.debug("Validation error: {}", errorMessage);
      }
    }
  }

  /**
   * Custom validation on {@link PersonDTO} for bulk upload, this is meant to supplement the
   * annotation based validation already in place. Validation errors are added to the DTO's message
   * list.
   *
   * @param personDto The person to check.
   * @return The collection of any validation errors, empty when there are no errors.
   */
  private List<ObjectError> validateForBulk(PersonDTO personDto) {
    return Collections.singletonList(new ObjectError(PERSON_DTO_NAME, "Not yet implemented."));
  }

  /**
   * Check public health number is already exists
   *
   * @param personDto
   * @return
   */
  private List<FieldError> checkPublicHealthNumber(PersonDTO personDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String publicHealthNumber = personDto.getPublicHealthNumber();
    if (StringUtils.containsWhitespace(publicHealthNumber)) {
      fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
          "publicHealthNumber should not contain any whitespaces"));
      return fieldErrors;
    }
    // Ignore if publicHealthNumber is N/A or UNKNOWN
    if (NA.equalsIgnoreCase(publicHealthNumber) || UNKNOWN.equalsIgnoreCase(publicHealthNumber)) {
      return fieldErrors;
    }
    if (personDto.getId() != null) {
      if (StringUtils.isNotEmpty(publicHealthNumber)) {
        List<Person> existingPersons = personRepository
            .findByPublicHealthNumber(publicHealthNumber);
        if (existingPersons.size() > 1) {
          fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
              String.format(
                  "publicHealthNumber %s is not unique, there are currently %d persons with this number: %s",
                  personDto.getPublicHealthNumber(), existingPersons.size(),
                  existingPersons)));
        } else if (existingPersons.size() == 1) {
          if (!personDto.getId().equals(existingPersons.get(0).getId())) {
            fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
                String.format(
                    "publicHealthNumber %s is not unique, there is currently one person with this number: %s",
                    personDto.getPublicHealthNumber(), existingPersons.get(0))));
          }
        }
      }
    } else {
      //if we create a person
      if (StringUtils.isNotEmpty(publicHealthNumber)) {
        List<Person> existingPersons = personRepository
            .findByPublicHealthNumber(publicHealthNumber);
        if (!existingPersons.isEmpty()) {
          fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
              String.format(
                  "publicHealthNumber %s is not unique, there is currently one person with this number: %s",
                  personDto.getPublicHealthNumber(), existingPersons.get(0))));
        }
      }
    }
    return fieldErrors;
  }


}
