package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProgrammeMembershipValidatorTest {

  private static final String DEFAULT_ROTATION_NAME = "rotation";
  private static final String DEFAULT_LEAVING_REASON = "leavingReason";
  private static final String DEFAULT_TRAINING_PATHWAY = "CCT";
  private static final String INVALID_TRAINING_PATHWAY = "AAA";
  private static final ProgrammeMembershipType DEFAULT_PROGRAMME_MEMBERSHIP_TYPE =
      ProgrammeMembershipType.ACADEMIC;

  private static final ProgrammeMembershipType INVALID_PROGRAMME_MEMBERSHIP_TYPE =
      ProgrammeMembershipType.FTSTA;
  private static final Long PROGRAMME_MEMBERSHIP_ID = 1L;
  private static final Long PROGRAMME_ID = 1L;
  private static final LocalDate START_DATE = LocalDate.of(2023, Month.AUGUST, 23);
  private static final LocalDate END_DATE = LocalDate.of(2024, Month.AUGUST, 23);
  @Mock
  RotationService rotationServiceMock;
  @Mock
  ReferenceService referenceServiceMock;
  @InjectMocks
  private ProgrammeMembershipValidator validator;

  @Test
  void shouldAddNoErrorsWhenAllValidationPasses() {
    ProgrammeMembershipDTO pmDto = new ProgrammeMembershipDTO();
    pmDto.setId(PROGRAMME_MEMBERSHIP_ID);
    pmDto.setProgrammeId(PROGRAMME_ID);
    pmDto.setProgrammeMembershipType(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE);
    RotationDTO rotationDto = new RotationDTO();
    rotationDto.setName(DEFAULT_ROTATION_NAME);
    pmDto.setRotation(rotationDto);
    pmDto.setProgrammeStartDate(START_DATE);
    pmDto.setProgrammeEndDate(END_DATE);
    pmDto.setLeavingReason(DEFAULT_LEAVING_REASON);
    pmDto.setTrainingPathway(DEFAULT_TRAINING_PATHWAY);
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setCurriculumStartDate(START_DATE);
    cmDto.setCurriculumEndDate(END_DATE);
    pmDto.setCurriculumMemberships(Lists.newArrayList(cmDto));

    when(rotationServiceMock.getCurrentRotationsByNameAndProgrammeId(DEFAULT_ROTATION_NAME,
        PROGRAMME_ID)).thenReturn(Lists.newArrayList(rotationDto));

    Map<String, String> leavingReasonsExistMap = new HashMap<>();
    leavingReasonsExistMap.put(DEFAULT_LEAVING_REASON, DEFAULT_LEAVING_REASON);
    when(referenceServiceMock.leavingReasonsMatch(Lists.newArrayList(DEFAULT_LEAVING_REASON),
        true)).thenReturn(leavingReasonsExistMap);

    Map<String, Boolean> programmeMembershipsExistMap = new HashMap<>();
    programmeMembershipsExistMap.put(DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.name(), true);
    when(referenceServiceMock.programmeMembershipTypesExist(Lists.newArrayList(
        DEFAULT_PROGRAMME_MEMBERSHIP_TYPE.name()), true)).thenReturn(programmeMembershipsExistMap);

    validator.validateForBulk(pmDto);
    assertEquals(0, pmDto.getMessageList().size());
  }

  @Test
  void shouldAddErrorsWhenValidationfails() {
    ProgrammeMembershipDTO pmDto = new ProgrammeMembershipDTO();
    pmDto.setId(PROGRAMME_MEMBERSHIP_ID);
    pmDto.setProgrammeId(PROGRAMME_ID);
    pmDto.setProgrammeMembershipType(INVALID_PROGRAMME_MEMBERSHIP_TYPE);
    RotationDTO rotationDto = new RotationDTO();
    rotationDto.setName(DEFAULT_ROTATION_NAME);
    pmDto.setRotation(rotationDto);
    pmDto.setProgrammeStartDate(START_DATE);
    pmDto.setProgrammeEndDate(END_DATE);
    pmDto.setLeavingReason(DEFAULT_LEAVING_REASON);
    pmDto.setTrainingPathway(INVALID_TRAINING_PATHWAY);
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setCurriculumStartDate(START_DATE.minusDays(1));
    cmDto.setCurriculumEndDate(END_DATE.plusDays(1));
    pmDto.setCurriculumMemberships(Lists.newArrayList(cmDto));

    // rotation not found
    when(rotationServiceMock.getCurrentRotationsByNameAndProgrammeId(DEFAULT_ROTATION_NAME,
        PROGRAMME_ID)).thenReturn(Lists.newArrayList());

    // leaving reason not exists
    Map<String, String> leavingReasonsExistMap = new HashMap<>();
    leavingReasonsExistMap.put(DEFAULT_LEAVING_REASON, "");
    when(referenceServiceMock.leavingReasonsMatch(Lists.newArrayList(DEFAULT_LEAVING_REASON),
        true)).thenReturn(leavingReasonsExistMap);

    // programme membership not exists
    Map<String, Boolean> programmeMembershipsExistMap = new HashMap<>();
    programmeMembershipsExistMap.put(INVALID_PROGRAMME_MEMBERSHIP_TYPE.name(), false);
    when(referenceServiceMock.programmeMembershipTypesExist(Lists.newArrayList(
        INVALID_PROGRAMME_MEMBERSHIP_TYPE.name()), true)).thenReturn(programmeMembershipsExistMap);

    validator.validateForBulk(pmDto);
    List<String> msgList = pmDto.getMessageList();
    assertEquals(6, msgList.size());
    assertThat(msgList, hasItem(containsString(
        ProgrammeMembershipValidator.PM_START_DATE_LATER_THAN_CM_START_DATE)));
    assertThat(msgList, hasItem(containsString(
        ProgrammeMembershipValidator.PM_END_DATE_EARLIER_THAN_CM_END_DATE)));
    assertThat(msgList, hasItem(containsString(
        String.format(ProgrammeMembershipValidator.TRAINING_PATHWAY_NOT_EXISTS,
            INVALID_TRAINING_PATHWAY))));
    assertThat(msgList, hasItem(containsString(
        String.format(ProgrammeMembershipValidator.PM_END_DATE_EARLIER_THAN_CM_END_DATE,
            DEFAULT_ROTATION_NAME, PROGRAMME_ID))));
    assertThat(msgList, hasItem(containsString(
        String.format(ProgrammeMembershipValidator.STRING_CODE_NOT_EXISTS,
            "Leaving reason", DEFAULT_LEAVING_REASON))));
    assertThat(msgList, hasItem(containsString(
        String.format(ProgrammeMembershipValidator.STRING_CODE_NOT_EXISTS,
            "Programme membership type", INVALID_PROGRAMME_MEMBERSHIP_TYPE))));
  }

  @Test
  void shouldValidateProgrammeDates() {
    ProgrammeMembershipDTO pmDto = new ProgrammeMembershipDTO();
    pmDto.setId(PROGRAMME_MEMBERSHIP_ID);

    pmDto.setProgrammeStartDate(START_DATE);
    pmDto.setProgrammeEndDate(END_DATE.minusYears(1).minusDays(1));

    validator.validateForBulk(pmDto);
    assertEquals(1, pmDto.getMessageList().size());
    assertThat(pmDto.getMessageList(), hasItem(containsString(
        ProgrammeMembershipValidator.PM_START_DATE_LATER_THAN_END_DATE)));
  }

  @Test
  void shouldAddErrorWhenMutipleRotationsFound() {
    ProgrammeMembershipDTO pmDto = new ProgrammeMembershipDTO();
    pmDto.setId(PROGRAMME_MEMBERSHIP_ID);
    pmDto.setProgrammeId(PROGRAMME_ID);
    pmDto.setProgrammeStartDate(START_DATE);
    pmDto.setProgrammeEndDate(END_DATE);
    RotationDTO rotationDto = new RotationDTO();
    rotationDto.setName(DEFAULT_ROTATION_NAME);
    pmDto.setRotation(rotationDto);

    RotationDTO rotationDto2 = new RotationDTO();

    when(rotationServiceMock.getCurrentRotationsByNameAndProgrammeId(DEFAULT_ROTATION_NAME,
        PROGRAMME_ID)).thenReturn(Lists.newArrayList(rotationDto, rotationDto2));

    validator.validateForBulk(pmDto);
    assertEquals(1, pmDto.getMessageList().size());
    assertThat(pmDto.getMessageList(), hasItem(containsString(
        String.format(ProgrammeMembershipValidator.MULTIPLE_ROTATIONS_FOUND,
            DEFAULT_ROTATION_NAME, PROGRAMME_ID))));
  }

  @Test
  void shouldNotCheckAgainWhenPmTypeNotExistErrorAlreadyExists() {
    ProgrammeMembershipDTO pmDto = new ProgrammeMembershipDTO();
    pmDto.setId(PROGRAMME_MEMBERSHIP_ID);
    pmDto.setProgrammeStartDate(START_DATE);
    pmDto.setProgrammeEndDate(END_DATE);
    pmDto.addMessage(String.format(ProgrammeMembershipValidator.STRING_CODE_NOT_EXISTS,
        "Programme membership type", INVALID_PROGRAMME_MEMBERSHIP_TYPE));

    validator.validateForBulk(pmDto);
    verify(referenceServiceMock, never()).programmeMembershipTypesExist(any(List.class),
        any(Boolean.class));
    assertEquals(1, pmDto.getMessageList().size());
  }
}
