package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.api.enumeration.TrainingPathway;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

  protected static final String ROTATION_NOT_EXiSTS =
      "Rotation with name (%s) does not exist for programmeId (%s).";
  protected static final String MULTIPLE_ROTATIONS_FOUND =
      "Multiple rotations with name (%s) found for programmeId (%s).";
  protected static final String PM_START_DATE_LATER_THAN_END_DATE =
      "Programme Start Date must be before the End Date";
  protected static final String PM_START_DATE_LATER_THAN_CM_START_DATE =
      "Programme start date must be before any curriculum start date.";
  protected static final String PM_END_DATE_EARLIER_THAN_CM_END_DATE =
      "Programme end date must be after any curriculum end date.";
  protected static final String STRING_CODE_NOT_EXISTS = "%s with code %s does not exist.";
  protected static final String TRAINING_PATHWAY_NOT_EXISTS =
      "Training pathway with code %s does not exist.";
  private static final String PROGRAMME_MEMBERSHIP_DTO_NAME = "ProgrammeMembershipDTO";
  private final PersonRepository personRepository;
  private final ProgrammeRepository programmeRepository;
  private final CurriculumRepository curriculumRepository;
  private final RotationService rotationService;
  private final ReferenceService referenceService;

  @Autowired
  public ProgrammeMembershipValidator(PersonRepository personRepository,
      ProgrammeRepository programmeRepository,
      CurriculumRepository curriculumRepository,
      RotationService rotationService,
      ReferenceService referenceService) {
    this.personRepository = personRepository;
    this.programmeRepository = programmeRepository;
    this.curriculumRepository = curriculumRepository;
    this.rotationService = rotationService;
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the programmeMembershipDTO DTO, this is meant to supplement the annotation
   * based validation already in place. It checks that the person, programme and curriculum
   * entered.
   *
   * @param programmeMembershipDto the programmeMembership to check
   * @return
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ProgrammeMembershipDTO programmeMembershipDto)
      throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(programmeMembershipDto));
    fieldErrors.addAll(checkProgramme(programmeMembershipDto));
    fieldErrors.addAll(checkCurriculum(programmeMembershipDto));
    fieldErrors.addAll(checkRotation(programmeMembershipDto));
    fieldErrors.addAll(checkProgrammeDates(programmeMembershipDto));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
          programmeMembershipDto, PROGRAMME_MEMBERSHIP_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  /**
   * Custom validation on the programmeMembershipDTO from bulk upload.
   *
   * @param programmeMembershipDto the Dto to check
   */
  public void validateForBulk(ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    fieldErrors.addAll(checkRotationExists(programmeMembershipDto));
    fieldErrors.addAll(checkProgrammeDates(programmeMembershipDto));
    fieldErrors.addAll(checkLeavingReason(programmeMembershipDto));
    fieldErrors.addAll(checkTrainingPathway(programmeMembershipDto));
    fieldErrors.addAll(checkProgrammeDatesWithCurriculumDates(programmeMembershipDto));
    fieldErrors.addAll(checkProgrammeMembershipType(programmeMembershipDto));

    fieldErrors.forEach(err -> {
      programmeMembershipDto.addMessage(err.getDefaultMessage());
    });
  }

  /**
   * Check if the role name exists for the programme id.
   *
   * @param programmeMembershipDto the Dto to check
   * @return a list of field errors
   */
  private List<FieldError> checkRotationExists(ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (programmeMembershipDto.getRotation() == null
        || StringUtils.isEmpty(programmeMembershipDto.getRotation().getName())) {
      return fieldErrors;
    }

    String rotationName = programmeMembershipDto.getRotation().getName();
    Long programmeId = programmeMembershipDto.getProgrammeId();
    List<RotationDTO> rotationDtos = rotationService.getCurrentRotationsByNameAndProgrammeId(
        rotationName, programmeId);
    if (rotationDtos.isEmpty()) {
      fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "rotation",
          String.format(ROTATION_NOT_EXiSTS, rotationName, programmeId)));
    } else if (rotationDtos.size() > 1) {
      fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "rotation",
          String.format(MULTIPLE_ROTATIONS_FOUND, rotationName, programmeId)));
    } else if (programmeMembershipDto.getRotation().getId() == null) {
      programmeMembershipDto.setRotation(rotationDtos.get(0));
    }
    return fieldErrors;
  }

  /**
   * Check if the programme start date is earlier than every curriculum start date.
   * And the programme end date is later than every curriculum end date.
   *
   * @param programmeMembershipDto the dto to check
   * @return a list of field errors
   */
  private List<FieldError> checkProgrammeDatesWithCurriculumDates(
      ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate pmStartDate = programmeMembershipDto.getProgrammeStartDate();
    LocalDate pmEndDate = programmeMembershipDto.getProgrammeEndDate();

    List<CurriculumMembershipDTO> curriculumMembershipDtos =
        programmeMembershipDto.getCurriculumMemberships();

    if (curriculumMembershipDtos != null) {
      curriculumMembershipDtos.forEach(cm -> {
        LocalDate cmStartDate = cm.getCurriculumStartDate();
        LocalDate cmEndDate = cm.getCurriculumEndDate();
        if (cmStartDate.isBefore(pmStartDate)) {
          fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "programmeStartDate",
              PM_START_DATE_LATER_THAN_CM_START_DATE));
        }
        if (cmEndDate.isAfter(pmEndDate)) {
          fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "programmeStartDate",
              PM_END_DATE_EARLIER_THAN_CM_END_DATE));
        }
      });
    }
    return fieldErrors;
  }

  /**
   * Check if programme membership type exists and is current in Reference service.
   * The enumeration helps do the first check, and this step would be a further check on Reference.
   *
   * @param programmeMembershipDto the dto to check
   * @return a list of field errors
   */
  private List<FieldError> checkProgrammeMembershipType(
      ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    ProgrammeMembershipType programmeMembershipType =
        programmeMembershipDto.getProgrammeMembershipType();

    if (programmeMembershipType != null) {
      Map<String, Boolean> programmeMembershipsExistMap =
          referenceService.programmeMembershipTypesExist(
              Collections.singletonList(programmeMembershipType.name()), true);
      notExistsStringFieldErrors(fieldErrors, programmeMembershipsExistMap,
          "programmeMembershipType", "Programme membership type");
    }
    return fieldErrors;
  }

  /**
   * Check if leaving reason exists and is current in Reference service.
   *
   * @param programmeMembershipDto the dto to check
   * @return a list of field errors
   */
  private List<FieldError> checkLeavingReason(ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String leavingReason = programmeMembershipDto.getLeavingReason();
    if (StringUtils.isNotEmpty(leavingReason)) {
      Map<String, Boolean> leavingReasonsExistMap = referenceService.leavingReasonsExist(
          Collections.singletonList(leavingReason), true);
      notExistsStringFieldErrors(fieldErrors, leavingReasonsExistMap, "leavingReason",
          "Leaving reason");
    }
    return fieldErrors;
  }

  private void notExistsStringFieldErrors(final List<FieldError> fieldErrors,
      final Map<String, Boolean> codesExistsMap,
      final String field, final String entityName) {
    codesExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, field,
            String.format(STRING_CODE_NOT_EXISTS, entityName, k)));
      }
    });
  }

  /**
   * Check if training pathway is allowed in the enumeration.
   *
   * @param programmeMembershipDto the dto to check
   * @return a list of field errors
   */
  private List<FieldError> checkTrainingPathway(ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String trainingPathway = programmeMembershipDto.getTrainingPathway();
    if (StringUtils.isNotEmpty(trainingPathway)) {
      TrainingPathway trainingPathwayEnum = TrainingPathway.fromString(
          programmeMembershipDto.getTrainingPathway());
      if (trainingPathwayEnum == null) {
        fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "trainingPathway",
            String.format(TRAINING_PATHWAY_NOT_EXISTS, trainingPathway)));
      }
    }
    return fieldErrors;
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
   * @param programmeMembershipDto return
   */
  private List<FieldError> checkProgrammeDates(ProgrammeMembershipDTO programmeMembershipDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate startDate = programmeMembershipDto.getProgrammeStartDate();
    LocalDate endDate = programmeMembershipDto.getProgrammeEndDate();

    if (startDate != null && startDate.isAfter(endDate)) {
      fieldErrors.add(new FieldError(PROGRAMME_MEMBERSHIP_DTO_NAME, "Programme Start Date",
          PM_START_DATE_LATER_THAN_END_DATE));
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
