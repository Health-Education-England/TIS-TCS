package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link ProgrammeMembership} that cannot be easily done
 * via annotations
 */
@Component
public class ProgrammeMembershipValidator {

  private static final String PROGRAMME_MEMBERSHIP_DTO_NAME = "ProgrammeMembershipDTO";

  private PersonRepository personRepository;
  private ProgrammeRepository programmeRepository;
  private CurriculumRepository curriculumRepository;
  private RotationService rotationService;

  @Autowired
  public ProgrammeMembershipValidator(PersonRepository personRepository,
      ProgrammeRepository programmeRepository,
      CurriculumRepository curriculumRepository,
      RotationService rotationService) {
    this.personRepository = personRepository;
    this.programmeRepository = programmeRepository;
    this.curriculumRepository = curriculumRepository;
    this.rotationService = rotationService;
  }

  /**
   * Custom validation on the programmeMembershipDTO DTO, this is meant to supplement the annotation
   * based validation already in place. It checks that the person, programme and curriculum
   * entered.
   *
   * @param programmeMembershipDto the programmeMembership to check
   * @throws MethodArgumentNotValidException if there are validation errors
   * @return
   */
  public void validate(ProgrammeMembershipDTO programmeMembershipDto)
      throws MethodArgumentNotValidException {


    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(programmeMembershipDto));
    fieldErrors.addAll(checkProgramme(programmeMembershipDto));
    fieldErrors.addAll(checkCurriculum(programmeMembershipDto));
    fieldErrors.addAll(checkRotation(programmeMembershipDto));
    checkProgrammeDates(fieldErrors, programmeMembershipDto);
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
          programmeMembershipDto, PROGRAMME_MEMBERSHIP_DTO_NAME);
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
    if (programmeMembershipDTO.getPerson() == null
        || programmeMembershipDTO.getPerson().getId() == null) {
      requireFieldErrors(fieldErrors, "person");
    } else if (programmeMembershipDTO.getPerson() != null
        && programmeMembershipDTO.getPerson().getId() != null) {
      if (!personRepository.existsById(programmeMembershipDTO.getPerson().getId())) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "person",
            String.format("Person with id %d does not exist",
                programmeMembershipDTO.getPerson().getId())));
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
    Long programmeId = programmeMembershipDTO.getProgrammeId();
    if (programmeId != null) {
      if (!programmeRepository.existsById(programmeId)) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "programmeId",
            String.format("Programme with id %s does not exist", programmeId)));
      }
    }
    return fieldErrors;
  }

  /**
   * Check programme start date is before finish.
   *
   * @param programmeMembershipDto
   * return
   */
  private void checkProgrammeDates(List<FieldError> fieldErrors,
                                                   ProgrammeMembershipDTO programmeMembershipDto) {

    LocalDate startDate = programmeMembershipDto.getProgrammeStartDate();
    LocalDate endDate = programmeMembershipDto.getProgrammeEndDate();

    if (startDate.isAfter(endDate)) {
      FieldError fieldError =
          new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "Programme Start Date",
              "Programme Start Date must be after the End Date");
      fieldErrors.add(fieldError);
      }
  }

  /**
   * Check curriculum with programme association and is exists
   *
   * @param programmeMembershipDTO
   * @return
   */
  private List<FieldError> checkCurriculum(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    Set<Long> curriculumIds = programmeMembershipDTO.getCurriculumMemberships().stream()
        .map(CurriculumMembershipDTO::getCurriculumId).collect(Collectors.toSet());
    if (!CollectionUtils.isEmpty(curriculumIds)) {
      curriculumIds.stream().forEach(curriculumId -> {
        if (!curriculumRepository.existsById(curriculumId)) {
          fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "curriculumId",
              String.format("Curriculum with id %s does not exist", curriculumId)));
        } else {
          checkProgrammeCurriculumAssociation(fieldErrors, programmeMembershipDTO.getProgrammeId(),
              curriculumId);
        }
      });

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
  private void checkProgrammeCurriculumAssociation(List<FieldError> fieldErrors, Long programmeId,
      Long curriculumId) {

    boolean isExists = programmeRepository
        .programmeCurriculumAssociationExists(programmeId, curriculumId);
    if (!isExists) {
      fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "curriculumId",
          String.format("The selected Programme and Curriculum are not linked. " +
              "They must be linked before a Programme Membership can be made", curriculumId)));
    }
  }

  /**
   * Check rotation exists
   *
   * @param programmeMembershipDTO
   * @return
   */
  private List<FieldError> checkRotation(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the rotation
    if (programmeMembershipDTO.getRotation() != null
        && programmeMembershipDTO.getRotation().getId() != null) {
      if (!rotationService.rotationExists(programmeMembershipDTO.getRotation().getId(),
          programmeMembershipDTO.getProgrammeId())) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "rotation",
            String.format("Rotation with name (%1$s) does not exist for programmeId (%2$s)",
                programmeMembershipDTO.getRotation(), programmeMembershipDTO.getProgrammeId())));
      }
    }
    return fieldErrors;
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
