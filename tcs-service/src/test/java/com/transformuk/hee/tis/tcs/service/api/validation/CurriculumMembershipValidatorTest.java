package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.CM_END_DATE_AFTER_PM_END_DATE;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.CM_START_DATE_AFTER_END_DATE;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.CM_START_DATE_BEFORE_PM_START_DATE;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.CURRICULUM_MEMBERSHIP_NOT_FOUND;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.EXISTING_CM_FOUND;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.FIELD_CM_END_DATE;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.FIELD_CM_ID;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.FIELD_CM_START_DATE;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.FIELD_CURRICULUM_ID;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.FIELD_PM_UUID;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.NO_MATCHING_CURRICULUM;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.NO_PROGRAMME_MEMBERSHIP_FOR_ID;
import static com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator.NULL_PROGRAMME_MEMBERSHIP_ID;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class CurriculumMembershipValidatorTest {

  private static final long CURRICULUM_ID = 1L;
  private static final long CM_ID = 111L;
  private static final long PROGRAMME_ID = 1L;
  private static final UUID PM_UUID = UUID.randomUUID();
  private static final UUID PM_UUID_2 = UUID.randomUUID();
  private static final LocalDate START_DATE_1 = LocalDate.of(2020, 1, 1);
  private static final LocalDate END_DATE_1 = LocalDate.of(2025, 1, 1);
  private static final LocalDate START_DATE_2 = LocalDate.of(2021, 1, 1);
  private static final LocalDate END_DATE_2 = LocalDate.of(2024, 1, 1);
  private CurriculumMembershipValidator cmValidator;
  @Mock
  private ProgrammeMembershipRepository pmRepository;
  @Mock
  private CurriculumMembershipRepository cmRepository;

  @BeforeEach
  void setUp() {
    cmValidator = new CurriculumMembershipValidator(pmRepository, cmRepository);
  }

  CurriculumMembershipDTO createDto(Long curriculumId, UUID pmUuid, LocalDate cmStartDate,
      LocalDate cmEndDate) {
    CurriculumMembershipDTO dto = new CurriculumMembershipDTO();
    dto.setCurriculumId(curriculumId);
    dto.setProgrammeMembershipUuid(pmUuid);
    dto.setCurriculumStartDate(cmStartDate);
    dto.setCurriculumEndDate(cmEndDate);
    return dto;
  }

  @Test
  void shouldThrowExceptionsWhenPmUuidNull() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, null, END_DATE_1, START_DATE_1);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(CurriculumMembershipDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_PM_UUID, NULL_PROGRAMME_MEMBERSHIP_ID);
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenDuplicateFound() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);

    CurriculumMembership duplicateCm = new CurriculumMembership();
    duplicateCm.setId(CM_ID);
    when(cmRepository.findByCurriculumIdAndPmUuidAndDates(CURRICULUM_ID, PM_UUID.toString(),
        START_DATE_1, END_DATE_1)).thenReturn(Lists.newArrayList(duplicateCm));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();
    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CURRICULUM_ID, String.format(EXISTING_CM_FOUND, "111"));
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenCmDatesNotValid() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, END_DATE_2, START_DATE_2);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);

    Programme programme = new Programme();
    programme.setId(PROGRAMME_ID);

    Curriculum curriculum1 = new Curriculum();
    curriculum1.setId(1L);
    curriculum1.setStatus(Status.CURRENT);
    ProgrammeCurriculum pc1 = new ProgrammeCurriculum();
    pc1.setCurriculum(curriculum1);

    programme.getCurricula().addAll(Lists.newArrayList(pc1));
    pm.setProgramme(programme);

    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();
    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CM_START_DATE, CM_START_DATE_AFTER_END_DATE);
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenPmUuidNotFound() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);
    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.empty());

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();

    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_PM_UUID, NO_PROGRAMME_MEMBERSHIP_FOR_ID);
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenCmDatesNotMatchPmDates() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_2);
    pm.setProgrammeEndDate(END_DATE_2);
    Programme programme = new Programme();
    programme.setId(PROGRAMME_ID);

    Curriculum curriculum1 = new Curriculum();
    curriculum1.setId(1L);
    curriculum1.setStatus(Status.CURRENT);
    ProgrammeCurriculum pc1 = new ProgrammeCurriculum();
    pc1.setCurriculum(curriculum1);

    programme.getCurricula().addAll(Lists.newArrayList(pc1));
    pm.setProgramme(programme);

    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();

    FieldError fieldError1 = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CM_START_DATE, CM_START_DATE_BEFORE_PM_START_DATE);
    FieldError fieldError2 = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CM_END_DATE, CM_END_DATE_AFTER_PM_END_DATE);
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(2));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2));
  }

  @Test
  void shouldThrowExceptionsWhenCurriculumNotMatchPm() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_2, END_DATE_2);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);

    Programme programme = new Programme();
    programme.setId(PROGRAMME_ID);

    Curriculum curriculum1 = new Curriculum();
    curriculum1.setId(1L);
    curriculum1.setStatus(Status.INACTIVE);
    ProgrammeCurriculum pc1 = new ProgrammeCurriculum();
    pc1.setCurriculum(curriculum1);

    Curriculum curriculum2 = new Curriculum();
    curriculum2.setId(2L);
    curriculum2.setStatus(Status.CURRENT);
    ProgrammeCurriculum pc2 = new ProgrammeCurriculum();
    pc2.setCurriculum(curriculum2);

    programme.getCurricula().addAll(Lists.newArrayList(pc1, pc2));
    pm.setProgramme(programme);

    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> cmValidator.validate(dto));

    // Then.
    BindingResult result = thrown.getBindingResult();

    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CURRICULUM_ID, String.format(NO_MATCHING_CURRICULUM, CURRICULUM_ID));
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItems(fieldError));
  }

  @Test
  void shouldNotThrowExceptionsWhenValidationPasses() {
    // Given.
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_2, END_DATE_2);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);

    Programme programme = new Programme();
    programme.setId(PROGRAMME_ID);

    Curriculum curriculum1 = new Curriculum();
    curriculum1.setId(1L);
    curriculum1.setStatus(Status.CURRENT);
    ProgrammeCurriculum pc1 = new ProgrammeCurriculum();
    pc1.setCurriculum(curriculum1);

    programme.getCurricula().addAll(Lists.newArrayList(pc1));
    pm.setProgramme(programme);

    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    // When.
    assertDoesNotThrow(() -> cmValidator.validate(dto));
  }

  @Test
  void shouldThrowExceptionsWhenCmDatesNotValid_ForPatch() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, END_DATE_2, START_DATE_2);
    dto.setId(CM_ID);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);
    pm.setUuid(PM_UUID);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CM_ID);
    cm.setProgrammeMembership(pm);

    when(cmRepository.findById(CM_ID)).thenReturn(Optional.of(cm));

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> cmValidator.validateForPatch(dto));

    BindingResult result = thrown.getBindingResult();
    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CM_START_DATE, CM_START_DATE_AFTER_END_DATE);
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenCurriculumMembershipDoesNotBelongToCorrectPM_ForPatch() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);
    dto.setId(CM_ID);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);
    pm.setUuid(PM_UUID_2);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CM_ID);
    cm.setProgrammeMembership(pm);

    when(cmRepository.findById(CM_ID)).thenReturn(Optional.of(cm));

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> cmValidator.validateForPatch(dto));

    BindingResult result = thrown.getBindingResult();
    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_PM_UUID, String.format(NO_PROGRAMME_MEMBERSHIP_FOR_ID, CM_ID));
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldThrowExceptionsWhenCMNotFound_ForPatch() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);
    dto.setId(CM_ID);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);
    pm.setUuid(PM_UUID_2);

    when(cmRepository.findById(CM_ID)).thenReturn(Optional.empty());

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> cmValidator.validateForPatch(dto));

    BindingResult result = thrown.getBindingResult();
    FieldError fieldError = new FieldError(CurriculumMembershipDTO.class.getSimpleName(),
        FIELD_CM_ID, String.format(CURRICULUM_MEMBERSHIP_NOT_FOUND, CM_ID));
    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(1));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError));
  }

  @Test
  void shouldNotThrowExceptionsWhenValidationPasses_ForPatch() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, null, END_DATE_2);
    dto.setId(CM_ID);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setProgrammeStartDate(START_DATE_1);
    pm.setProgrammeEndDate(END_DATE_1);
    pm.setUuid(PM_UUID);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CM_ID);
    cm.setCurriculumStartDate(START_DATE_2);
    cm.setProgrammeMembership(pm);

    when(cmRepository.findById(CM_ID)).thenReturn(Optional.of(cm));

    assertDoesNotThrow(() -> cmValidator.validateForPatch(dto));
    assertThat(START_DATE_2, is(dto.getCurriculumStartDate()));
  }
}
