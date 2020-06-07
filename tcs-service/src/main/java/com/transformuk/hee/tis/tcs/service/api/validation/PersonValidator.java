package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonValidator.class);

  private static final String PERSON_DTO_NAME = "PersonDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";
  private final PersonRepository personRepository;
  private final ReferenceService referenceService;
  private final ContactDetailsValidator contactDetailsValidator;
  private final GdcDetailsValidator gdcDetailsValidator;
  private final GmcDetailsValidator gmcDetailsValidator;
  private final PersonalDetailsValidator personalDetailsValidator;
  private final RightToWorkValidator rightToWorkValidator;
  private final TrainerApprovalValidator trainerApprovalValidator;

  public PersonValidator(PersonRepository personRepository, ReferenceService referenceService,
      ContactDetailsValidator contactDetailsValidator, GdcDetailsValidator gdcDetailsValidator,
      GmcDetailsValidator gmcDetailsValidator, PersonalDetailsValidator personalDetailsValidator,
      RightToWorkValidator rightToWorkValidator,
      TrainerApprovalValidator trainerApprovalValidator) {
    this.personRepository = personRepository;
    this.referenceService = referenceService;
    this.contactDetailsValidator = contactDetailsValidator;
    this.gdcDetailsValidator = gdcDetailsValidator;
    this.gmcDetailsValidator = gmcDetailsValidator;
    this.personalDetailsValidator = personalDetailsValidator;
    this.rightToWorkValidator = rightToWorkValidator;
    this.trainerApprovalValidator = trainerApprovalValidator;
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
    fieldErrors.addAll(checkPerson(personDto));
    fieldErrors.addAll(checkPublicHealthNumber(personDto));
    fieldErrors.addAll(checkRole(personDto));

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
      List<FieldError> errors = validateForBulk(personDto);

      if (errors.isEmpty()) {
        continue;
      }

      LOGGER.debug("Validation errors occurred for Person: {}", personDto);

      for (FieldError error : errors) {
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
  private List<FieldError> validateForBulk(PersonDTO personDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    fieldErrors.addAll(checkPerson(personDto));
    fieldErrors.addAll(checkPublicHealthNumber(personDto));
    List<FieldError> checkRoleFieldError = checkRole(personDto);
    if (checkRoleFieldError.size() == 0) {
      fieldErrors.addAll(checkRoleForTrainerApproval(personDto));
    } else {
      fieldErrors.addAll(checkRoleFieldError);
    }
    fieldErrors.addAll(contactDetailsValidator.validateForBulk(personDto.getContactDetails()));
    fieldErrors.addAll(gdcDetailsValidator.validateForBulk(personDto.getGdcDetails()));
    fieldErrors.addAll(gmcDetailsValidator.validateForBulk(personDto.getGmcDetails()));
    fieldErrors.addAll(personalDetailsValidator.validateForBulk(personDto.getPersonalDetails()));
    fieldErrors.addAll(rightToWorkValidator.validateForBulk(personDto.getRightToWork()));
    personDto.getTrainerApprovals()
        .forEach(ta -> fieldErrors.addAll(trainerApprovalValidator.validateForBulk(ta)));

    return fieldErrors;
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

    if (StringUtils.isNotEmpty(publicHealthNumber)) {
      List<Person> existingPersons = personRepository
          .findByPublicHealthNumber(publicHealthNumber);

      if (existingPersons.size() > 0
          && personDto.getId() != null) { // should exclude the current one when update
        existingPersons.removeIf(r -> r.getId() == personDto.getId());
      }

      int existingSize = existingPersons.size();
      if (existingSize > 0) {
        fieldErrors.add(new FieldError(PERSON_DTO_NAME, "publicHealthNumber",
            String.format(
                "publicHealthNumber %s is not unique, there %s currently %d %s with this number (Person ID: %s)",
                personDto.getPublicHealthNumber(), existingSize > 1 ? "are" : "is", existingSize,
                existingSize > 1 ? "persons" : "person",
                existingPersons.stream().map(r -> r.getId().toString()).collect(
                    Collectors.joining(","))
            )));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkRole(PersonDTO personDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    String roleMultiValue = personDto.getRole();

    if (!StringUtils.isEmpty(roleMultiValue)) {
      // bulk upload uses a ';' separator
      roleMultiValue = roleMultiValue.replaceAll("\\s*;\\s*", ",");
      // remove the ',' in the end if it exists
      roleMultiValue = roleMultiValue.replaceAll(",$", "");
      personDto.setRole(roleMultiValue);

      String[] roles = roleMultiValue.split(",");
      Map<String, Boolean> rolesExist = referenceService.rolesExist(Arrays.asList(roles), true);

      for (String role : roles) {
        if (!rolesExist.getOrDefault(role, false)) {
          fieldErrors.add(new FieldError(PERSON_DTO_NAME, "role",
              String.format("role '%s' did not match a reference value.", role)));
        }
      }
    }

    return fieldErrors;
  }

  private List<FieldError> checkPerson(PersonDTO personDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // check the Person
    Long id = personDto.getId();
    if (id != null) {
      if (!personRepository.existsById(id)) {
        fieldErrors.add(new FieldError(PERSON_DTO_NAME, "person",
            String.format("Person with id %d does not exist", id)));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkRoleForTrainerApproval(PersonDTO personDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String role = personDto.getRole();
    String roleForCheck = "";

    if (!StringUtils.isEmpty(role)) { // new role exists
      roleForCheck = role;
    } else {
      // no new role, use the existing role
      Set<TrainerApprovalDTO> trainerApprovalDTO = personDto.getTrainerApprovals();
      if (trainerApprovalDTO != null && !trainerApprovalDTO.isEmpty()) {
        Person existingPerson = personRepository.findById(personDto.getId()).get();
        if (existingPerson != null && !StringUtils.isEmpty(existingPerson.getRole())) {
          roleForCheck = existingPerson.getRole();
        }
      }
    }

    // check role categories
    if (!StringUtils.isEmpty(roleForCheck)) {
      List<RoleDTO> roleDtos = referenceService.findRolesIn(roleForCheck);
      boolean eligibleForTrainerApproval =
          roleDtos.stream().filter(roleDTO -> roleDTO.getRoleCategory().getId() != 3).count() > 0;
      if (!eligibleForTrainerApproval && personDto.getTrainerApprovals().size() != 0) {
        fieldErrors.add(new FieldError(PERSON_DTO_NAME, "role",
            "To have a Trainer Approval, the role should contain at least one of 'Educational supervisors/Clinical supervisors/Leave approvers' categories"));
      }
    }

    return fieldErrors;
  }
}
