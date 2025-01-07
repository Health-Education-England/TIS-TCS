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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  private static final String CURRICULUM_MEMBERSHIP_DTO_NAME = "CurriculumMembershipDTO";

  protected static final String CM_STARTDATE_AFTER_ENDDATE =
      "Curriculum membership start date must not be later than end date.";
  protected static final String NO_PROGRAMME_MEMBERSHIP_FOR_ID =
      "Could not find the programme membership.";

  protected static final String CM_STARTDATE_BEFORE_PM_STARTDATE =
      "Curriculum membership start date must not be earlier than the "
          + "programme membership start date.";

  protected static final String CM_ENDDATE_AFTER_PM_ENDDATE =
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

  public void validate(CurriculumMembershipDTO cmDto) throws MethodArgumentNotValidException  {
    List<FieldError> fieldErrors = new ArrayList<>();

    fieldErrors.addAll(checkCmDates(cmDto));
    fieldErrors.addAll(checkCmWithPm(cmDto));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
          cmDto, CURRICULUM_MEMBERSHIP_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkCmWithPm(CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    UUID pmUuid = cmDto.getProgrammeMembershipUuid();
    Optional<ProgrammeMembership> optionalPm = pmRepository.findByUuid(pmUuid);
    if(optionalPm.isPresent()) {
      ProgrammeMembership pm = optionalPm.get();
      checkCmDatesWithPm(pm, cmDto);
      checkCurriculumIdWithProgramme(pm.getProgramme().getId(), cmDto.getCurriculumId());
    } else {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "ProgrammeMembership UUID",
          NO_PROGRAMME_MEMBERSHIP_FOR_ID));
    }
    return fieldErrors;
  }

  private List<FieldError> checkCurriculumIdWithProgramme(Long programmeId, Long curriculumId) {
    List<FieldError> fieldErrors = new ArrayList<>();
    Optional<Programme> optionalProgramme = programmeRepository.findById(programmeId);
    if (optionalProgramme.isPresent()) {
      Programme programme = optionalProgramme.get();
      Optional<ProgrammeCurriculum> optionalPc = programme.getCurricula().stream().filter(pc -> {
        Curriculum curriculum = pc.getCurriculum();
         return curriculum.getId().equals(curriculumId) && curriculum.getStatus() == Status.CURRENT;
      }).findAny();
      if (!optionalPc.isPresent()) {
        fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "Curriculum Id",
            NO_MATCHING_CURRICULUM));
      }
    } else {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "ProgrammeMembership Uuid",
          NO_PROGRAMME_INFO));
    }
    return fieldErrors;
  }

  private List<FieldError> checkCmDatesWithPm(ProgrammeMembership pm, CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    LocalDate cmStartDate = cmDto.getCurriculumStartDate();
    LocalDate cmEndDate = cmDto.getCurriculumEndDate();
    LocalDate pmStartDate = pm.getProgrammeStartDate();
    LocalDate pmEndDate = pm.getProgrammeEndDate();

    if (cmStartDate.isBefore(pmStartDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "Curriculum Start Date",
          CM_STARTDATE_BEFORE_PM_STARTDATE));
    }
    if (cmEndDate.isAfter(pmEndDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "Curriculum End Date",
          CM_ENDDATE_AFTER_PM_ENDDATE));
    }
    return fieldErrors;
  }

  private List<FieldError> checkCmDates(CurriculumMembershipDTO cmDto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate cmStartDate = cmDto.getCurriculumStartDate();
    LocalDate cmEndDate = cmDto.getCurriculumEndDate();
    if (cmStartDate.isAfter(cmEndDate)) {
      fieldErrors.add(new FieldError(CURRICULUM_MEMBERSHIP_DTO_NAME, "Curriculum Start Date",
          CM_STARTDATE_AFTER_ENDDATE));
    }
    return fieldErrors;
  }
}
