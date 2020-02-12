package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link Person} that cannot be easily done via
 * annotations
 */
@Component
public class PersonValidator {

  private static final String PERSON_DTO_NAME = "PersonDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";
  private PersonRepository personRepository;

  public PersonValidator(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   * Custom validation on the personDTO DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the public health number unique check if it is
   * entered.
   *
   * @param personDTO the person to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(PersonDTO personDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPublicHealthNumber(personDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(personDTO,
          PERSON_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      Method method = this.getClass().getMethods()[0];
      throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
    }
  }

  /**
   * Check public health number is already exists
   *
   * @param personDTO
   * @return
   */
  private List<FieldError> checkPublicHealthNumber(PersonDTO personDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String publicHealthNumber = personDTO.getPublicHealthNumber();
    if (StringUtils.containsWhitespace(publicHealthNumber)) {
      fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber", "publicHealthNumber should not contain any whitespaces"));
      return fieldErrors;
    }
    // Ignore if publicHealthNumber is N/A or UNKNOWN
    if (NA.equalsIgnoreCase(publicHealthNumber) || UNKNOWN.equalsIgnoreCase(publicHealthNumber)) {
      return fieldErrors;
    }
    if (personDTO.getId() != null) {
      if (StringUtils.isNotEmpty(publicHealthNumber)) {
        List<Person> existingPersons = personRepository
            .findByPublicHealthNumber(publicHealthNumber);
        if (existingPersons.size() > 1) {
          fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
              String.format(
                  "publicHealthNumber %s is not unique, there are currently %d persons with this number: %s",
                  personDTO.getPublicHealthNumber(), existingPersons.size(),
                  existingPersons)));
        } else if (existingPersons.size() == 1) {
          if (!personDTO.getId().equals(existingPersons.get(0).getId())) {
            fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
                String.format(
                    "publicHealthNumber %s is not unique, there is currently one person with this number: %s",
                    personDTO.getPublicHealthNumber(), existingPersons.get(0))));
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
                  personDTO.getPublicHealthNumber(), existingPersons.get(0))));
        }
      }
    }
    return fieldErrors;
  }


}
