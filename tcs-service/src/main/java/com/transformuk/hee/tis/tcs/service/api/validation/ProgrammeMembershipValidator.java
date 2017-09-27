package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Holds more complex custom validation for a {@link ProgrammeMembership} that
 * cannot be easily done via annotations
 */
@Component
public class ProgrammeMembershipValidator {

  private static final String PROGRAMME_MEMBERSHIP_DTO_NAME = "ProgrammeMembershipDTO";

  private PersonRepository personRepository;
  private ProgrammeRepository programmeRepository;
  private CurriculumRepository curriculumRepository;

  @Autowired
  public ProgrammeMembershipValidator(PersonRepository personRepository,
                                      ProgrammeRepository programmeRepository,
                                      CurriculumRepository curriculumRepository) {
    this.personRepository = personRepository;
    this.programmeRepository = programmeRepository;
    this.curriculumRepository = curriculumRepository;
  }

  /**
   * Custom validation on the programmeMembershipDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the person, programme and curriculum entered.
   *
   * @param programmeMembershipDTO the programmeMembership to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ProgrammeMembershipDTO programmeMembershipDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(programmeMembershipDTO));
    fieldErrors.addAll(checkProgramme(programmeMembershipDTO));
    fieldErrors.addAll(checkCurriculum(programmeMembershipDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(programmeMembershipDTO, PROGRAMME_MEMBERSHIP_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  /**
   * Check the person is assigned
   *
   * @param programmeMembershipDTO
   * @return
   */
  private List<FieldError> checkPerson(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // check the Person
    if (programmeMembershipDTO.getPerson() == null || programmeMembershipDTO.getPerson().getId() == null) {
      requireFieldErrors(fieldErrors, "person");
    } else if (programmeMembershipDTO.getPerson() != null && programmeMembershipDTO.getPerson().getId() != null) {
      if (!personRepository.exists(programmeMembershipDTO.getPerson().getId())) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "person",
            String.format("Person with id %d does not exist", programmeMembershipDTO.getPerson().getId())));
      }
    }
    return fieldErrors;
  }

  /**
   * Check programme is exists
   *
   * @param programmeMembershipDTO
   * @return
   */
  private List<FieldError> checkProgramme(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String programmeId = programmeMembershipDTO.getProgrammeId();
    if (programmeId != null) {
      if (!programmeRepository.exists(Long.valueOf(programmeId))) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "programmeId",
            String.format("Programme with id %s does not exist", programmeId)));
      }
    }
    return fieldErrors;
  }

  /**
   * Check curriculum with programme association and is exists
   *
   * @param programmeMembershipDTO
   * @return
   */
  private List<FieldError> checkCurriculum(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String curriculumId = programmeMembershipDTO.getCurriculumId();
    if (curriculumId != null) {
      if (!curriculumRepository.exists(Long.valueOf(curriculumId))) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "curriculumId",
            String.format("Curriculum with id %s does not exist", curriculumId)));
      } else {
        checkProgrammeCurriculumAssociation(fieldErrors, programmeMembershipDTO.getProgrammeId(), curriculumId);
      }
    }
    return fieldErrors;
  }

  /**
   * Check programme and curriculum association
   *
   * @param fieldErrors
   * @param programmeId
   * @param curriculumId
   */
  private void checkProgrammeCurriculumAssociation(List<FieldError> fieldErrors, String programmeId, String curriculumId) {
    Programme programme = programmeRepository.findOne(Long.valueOf(programmeId));
    if (programme != null && programme.getCurricula() != null) {
      Set<Curriculum> curricula = programme.getCurricula();
      Optional<Curriculum> optional = curricula.stream().
          filter(c -> c.getId().equals(Long.valueOf(curriculumId))).findFirst();
      if (!optional.isPresent()) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "curriculumId",
            String.format("Curriculum with id %s does associated with programme", curriculumId)));
      }
    }
  }

  /**
   * Check required fields
   *
   * @param fieldErrors
   * @param field
   */
  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
