package com.transformuk.hee.tis.tcs.service.api.validation;


import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Holds more complex custom validation for a {@link Qualification} that
 * cannot be easily done via annotations
 */
@Component
public class QualificationValidator {

  private static final String QUALIFICATION_DTO_NAME = "QualificationDTO";

  private PersonRepository personRepository;
  private ReferenceServiceImpl referenceService;

  @Autowired
  public QualificationValidator(PersonRepository personRepository, ReferenceServiceImpl referenceService) {
    this.personRepository = personRepository;
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the qualificationDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the person, medical school and country of qualification.
   *
   * @param qualificationDTO the qualification to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(QualificationDTO qualificationDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(qualificationDTO));
    fieldErrors.addAll(checkMedicalSchool(qualificationDTO));
    fieldErrors.addAll(checkCountryOfQualification(qualificationDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(qualificationDTO, QUALIFICATION_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkPerson(QualificationDTO qualificationDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the curricula
    if (qualificationDTO.getPerson() == null || qualificationDTO.getPerson().getId() == null) {
      requireFieldErrors(fieldErrors, "person");
    } else if (qualificationDTO.getPerson() != null && qualificationDTO.getPerson().getId() != null) {
      if (!personRepository.exists(qualificationDTO.getPerson().getId())) {
        fieldErrors.add(new FieldError(QUALIFICATION_DTO_NAME, "person",
            String.format("Person with id %d does not exist", qualificationDTO.getPerson().getId())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkMedicalSchool(QualificationDTO qualificationDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the sites
    if (StringUtils.isNotEmpty(qualificationDTO.getMedicalSchool())) {
      List<String> medicalSchools = Lists.newArrayList(qualificationDTO.getMedicalSchool());

      if (!CollectionUtils.isEmpty(medicalSchools)) {
        Map<String, Boolean> medicalSchoolExistsMap = referenceService.medicalSchoolsExists(medicalSchools);
        medicalSchoolExistsMap.forEach((k, v) -> {
          if (!v) {
            fieldErrors.add(new FieldError(QUALIFICATION_DTO_NAME, "medicalSchool",
                String.format("%s with id %s does not exist", "qualification", k)));
          }
        });
      }

    }
    return fieldErrors;
  }


  private List<FieldError> checkCountryOfQualification(QualificationDTO qualificationDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the sites
    if (StringUtils.isNotEmpty(qualificationDTO.getCountryOfQualification())) {
      List<String> countryOfQualifications = Lists.newArrayList(qualificationDTO.getCountryOfQualification());

      if (!CollectionUtils.isEmpty(countryOfQualifications)) {
        Map<String, Boolean> countryOfQualificationsExistsMap = referenceService.countryExists(countryOfQualifications);
        countryOfQualificationsExistsMap.forEach((k, v) -> {
          if (!v) {
            fieldErrors.add(new FieldError(QUALIFICATION_DTO_NAME, "countryOfQualification",
                String.format("%s with id %s does not exist", "countryOfQualification", k)));
          }
        });
      }

    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(QUALIFICATION_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
