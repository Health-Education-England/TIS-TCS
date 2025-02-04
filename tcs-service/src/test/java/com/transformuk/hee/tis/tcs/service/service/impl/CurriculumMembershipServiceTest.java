package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class CurriculumMembershipServiceTest {

  private static final long CURRICULUM_ID = 1L;
  private static final long CM_ID = 1L;
  private static final UUID PM_UUID = UUID.randomUUID();
  private static final LocalDate START_DATE_1 = LocalDate.of(2020, 1, 1);
  private static final LocalDate END_DATE_1 = LocalDate.of(2025, 1, 1);
  private static final LocalDate START_DATE_2 = START_DATE_1.plusDays(1);
  private static final LocalDate END_DATE_2 = END_DATE_1.minusDays(1);
  @Mock
  private CurriculumMembershipRepository cmRepository;
  @Mock
  private ProgrammeMembershipRepository pmRepository;
  @Mock
  private CurriculumMembershipMapper cmMapper;
  @Captor
  private ArgumentCaptor<CurriculumMembership> cmArgCaptor;

  private CurriculumMembershipService cmService;

  @BeforeEach
  void setUp() {
    cmService = new CurriculumMembershipServiceImpl(cmRepository, cmMapper, pmRepository);
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
  void shouldSaveCmDto() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CURRICULUM_ID);
    cm.setCurriculumStartDate(START_DATE_1);
    cm.setCurriculumEndDate(END_DATE_1);
    cm.setCurriculumId(CURRICULUM_ID);
    when(cmMapper.toEntity(dto)).thenReturn(cm);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setUuid(PM_UUID);
    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    CurriculumMembership returnedCm = new CurriculumMembership();
    returnedCm.setId(CURRICULUM_ID);
    returnedCm.setCurriculumStartDate(START_DATE_1);
    returnedCm.setCurriculumEndDate(END_DATE_1);
    returnedCm.setCurriculumId(CURRICULUM_ID);
    returnedCm.setId(CM_ID);
    returnedCm.setProgrammeMembership(pm);

    CurriculumMembershipDTO returnedCmDto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1,
        END_DATE_1);
    returnedCmDto.setId(CM_ID);

    when(cmRepository.save(any(CurriculumMembership.class))).thenReturn(returnedCm);
    when(cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm)).thenReturn(
        returnedCmDto);

    CurriculumMembershipDTO result = cmService.save(dto);
    assertEquals(returnedCmDto, result);
  }

  @Test
  void shouldThrowExceptionWhenPmNotFound() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_1, END_DATE_1);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CURRICULUM_ID);
    cm.setCurriculumStartDate(START_DATE_1);
    cm.setCurriculumEndDate(END_DATE_1);
    cm.setCurriculumId(CURRICULUM_ID);
    when(cmMapper.toEntity(dto)).thenReturn(cm);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setUuid(PM_UUID);
    when(pmRepository.findByUuid(PM_UUID)).thenThrow(
        new NoSuchElementException("No value present"));

    assertThrows(NoSuchElementException.class,
        () -> cmService.save(dto));
  }

  @Test
  void shouldPatchCmDto() throws MethodArgumentNotValidException, NoSuchMethodException {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_2, END_DATE_2);
    dto.setId(CM_ID);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CM_ID);
    cm.setCurriculumStartDate(START_DATE_1);
    cm.setCurriculumEndDate(END_DATE_1);
    cm.setCurriculumId(CURRICULUM_ID);

    CurriculumMembership returnedCm = new CurriculumMembership();
    returnedCm.setCurriculumStartDate(START_DATE_2);
    returnedCm.setCurriculumEndDate(END_DATE_2);
    returnedCm.setCurriculumId(CURRICULUM_ID);
    returnedCm.setId(CM_ID);

    CurriculumMembershipDTO returnedCmDto = createDto(CURRICULUM_ID, PM_UUID, START_DATE_2,
        END_DATE_2);
    returnedCmDto.setId(CM_ID);

    when(cmRepository.findById(dto.getId())).thenReturn(Optional.of(cm));

    when(cmRepository.save(cmArgCaptor.capture())).thenReturn(returnedCm);
    when(cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm)).thenReturn(
        returnedCmDto);

    CurriculumMembershipDTO result = cmService.patch(dto);
    assertEquals(returnedCmDto, result);
    CurriculumMembership cmToSave = cmArgCaptor.getValue();
    assertEquals(START_DATE_2, cmToSave.getCurriculumStartDate());
    assertEquals(END_DATE_2, cmToSave.getCurriculumEndDate());
  }

  @Test
  void shouldThrowExceptionWhenCMNotFound() {
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setId(CM_ID);
    cmDto.setCurriculumStartDate(START_DATE_1);
    cmDto.setCurriculumEndDate(END_DATE_1);
    cmDto.setProgrammeMembershipUuid(PM_UUID);

    when(cmRepository.findById(cmDto.getId())).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> {
      cmService.patch(cmDto);
    });
    verify(cmRepository, times(1)).findById(cmDto.getId());
  }
}
