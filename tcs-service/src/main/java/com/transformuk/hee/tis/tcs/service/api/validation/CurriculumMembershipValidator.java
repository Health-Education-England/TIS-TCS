package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link CurriculumMembership} that cannot be easily
 * done via annotations
 */
@Component
public class CurriculumMembershipValidator {

  protected static final String CURRICULUM_MEMBERSHIP_DTO_NAME = "CurriculumMembershipDTO";
  protected static final String FIELD_CURRICULUM_ID = "Curriculum Id";
  protected static final String FIELD_PM_UUID = "ProgrammeMembership Uuid";
  protected static final String FIELD_CM_START_DATE = "Curriculum Start Date";
  protected static final String FIELD_CM_END_DATE = "Curriculum End Date";

  protected static final String CM_START_DATE_AFTER_END_DATE =
      "Curriculum membership start date must not be later than end date.";
  protected static final String NULL_PROGRAMME_MEMBERSHIP_ID =
      "Programme membership UUID cannot be null";
  protected static final String NO_PROGRAMME_MEMBERSHIP_FOR_ID =
      "Could not find the programme membership.";

  protected static final String CM_START_DATE_BEFORE_PM_START_DATE =
      "Curriculum membership start date must not be earlier than the "
          + "programme membership start date.";

  protected static final String CM_END_DATE_AFTER_PM_END_DATE =
      "Curriculum membership end date must not be later than the programme membership end date.";

  protected static final String NO_MATCHING_CURRICULUM =
      "Could not find current curriculum for id \"%s\" under the programme.";

  protected static final String NO_PROGRAMME_INFO =
      "Could not retrieve the programme info.";

  private final ProgrammeMembershipRepository pmRepository;
  private final ProgrammeRepository programmeRepository;

  public CurriculumMembershipValidator(ProgrammeMembershipRepository pmRepository,
      ProgrammeRepository programmeRepository) {
    this.pmRepository = pmRepository;
    this.programmeRepository = programmeRepository;
  }

  /**
   * Validate a CurriculumMembershipDTO.
   *
   * @param cmDto the curriculumMembership Dto to validate
   * @throws MethodArgumentNotValidException
   */
  public void validate(CurriculumMembershipDTO cmDto)
      throws MethodArgumentNotValidException, NoSuchMethodException {
    List<FieldError> fieldErrors = new ArrayList<>();

    fieldErrors.addAll(checkCmDates(cmDto));
    fieldErrors.addAll(checkCmWithPm(cmDto));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
          cmDto, CURRICULUM_MEMBERSHIP_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      Method method = this.getClass().getMethod("validate", CurriculumMembershipDTO.class);
      MethodParameter methodParameter = new MethodParameter(method, 0);
      throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
  }

  private List<FieldError> checkCmWithPm(CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    UUID pmUuid = cmDto.getProgrammeMembershipUuid();
    if (pmUuid == null) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_PM_UUID,
          NULL_PROGRAMME_MEMBERSHIP_ID));
    } else {
      Optional<ProgrammeMembership> optionalPm = pmRepository.findByUuid(pmUuid);
      if (optionalPm.isPresent()) {
        ProgrammeMembership pm = optionalPm.get();
        fieldErrors.addAll(checkCmDatesWithPm(pm, cmDto));
        fieldErrors.addAll(
            checkCurriculumIdWithProgramme(pm.getProgramme().getId(), cmDto.getCurriculumId()));
      } else {
        fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_PM_UUID,
            NO_PROGRAMME_MEMBERSHIP_FOR_ID));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkCurriculumIdWithProgramme(Long programmeId, Long curriculumId) {
    List<FieldError> fieldErrors = new ArrayList<>();
    Optional<Programme> optionalProgramme = programmeRepository.findProgrammeByIdEagerFetch(
        programmeId);
    if (optionalProgramme.isPresent()) {
      Programme programme = optionalProgramme.get();
      Optional<ProgrammeCurriculum> optionalPc = programme.getCurricula().stream().filter(pc -> {
        Curriculum curriculum = pc.getCurriculum();
        return curriculum.getId().equals(curriculumId) && curriculum.getStatus() == Status.CURRENT;
      }).findAny();
      if (!optionalPc.isPresent()) {
        fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_CURRICULUM_ID,
            String.format(NO_MATCHING_CURRICULUM, curriculumId)));
      }
    } else {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_PM_UUID,
          NO_PROGRAMME_INFO));
    }
    return fieldErrors;
  }

  private List<FieldError> checkCmDatesWithPm(ProgrammeMembership pm,
      CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    LocalDate cmStartDate = cmDto.getCurriculumStartDate();
    LocalDate cmEndDate = cmDto.getCurriculumEndDate();
    LocalDate pmStartDate = pm.getProgrammeStartDate();
    LocalDate pmEndDate = pm.getProgrammeEndDate();

    if (cmStartDate.isBefore(pmStartDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_CM_START_DATE,
          CM_START_DATE_BEFORE_PM_START_DATE));
    }
    if (cmEndDate.isAfter(pmEndDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_CM_END_DATE,
          CM_END_DATE_AFTER_PM_END_DATE));
    }
    return fieldErrors;
  }

  private List<FieldError> checkCmDates(CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate cmStartDate = cmDto.getCurriculumStartDate();
    LocalDate cmEndDate = cmDto.getCurriculumEndDate();
    if (cmStartDate.isAfter(cmEndDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, FIELD_CM_START_DATE,
          CM_START_DATE_AFTER_END_DATE));
    }
    return fieldErrors;
  }
}
