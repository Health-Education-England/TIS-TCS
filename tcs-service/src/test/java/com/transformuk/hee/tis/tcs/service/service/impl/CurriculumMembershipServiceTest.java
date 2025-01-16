package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurriculumMembershipServiceTest {

  private static final long CURRICULUM_ID = 1L;
  private static final long CM_ID = 1L;
  private static final UUID PM_UUID = UUID.randomUUID();
  private static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDate END_DATE = LocalDate.of(2025, 1, 1);
  @Mock
  private CurriculumMembershipRepository cmRepository;
  @Mock
  private ProgrammeMembershipRepository pmRepository;
  @Mock
  private CurriculumMembershipMapper cmMapper;

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
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE, END_DATE);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CURRICULUM_ID);
    cm.setCurriculumStartDate(START_DATE);
    cm.setCurriculumEndDate(END_DATE);
    cm.setCurriculumId(CURRICULUM_ID);
    when(cmMapper.toEntity(dto)).thenReturn(cm);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setUuid(PM_UUID);
    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    CurriculumMembership returnedCm = new CurriculumMembership();
    returnedCm.setId(CURRICULUM_ID);
    returnedCm.setCurriculumStartDate(START_DATE);
    returnedCm.setCurriculumEndDate(END_DATE);
    returnedCm.setCurriculumId(CURRICULUM_ID);
    returnedCm.setId(CM_ID);
    returnedCm.setProgrammeMembership(pm);

    CurriculumMembershipDTO returnedCmDto = createDto(CURRICULUM_ID, PM_UUID, START_DATE, END_DATE);
    returnedCmDto.setId(CM_ID);

    when(cmRepository.save(any(CurriculumMembership.class))).thenReturn(returnedCm);
    when(cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm)).thenReturn(
        returnedCmDto);

    CurriculumMembershipDTO result = cmService.save(dto);
    assertEquals(returnedCmDto, result);
  }

  @Test
  void shouldThrowExceptionWhenPmNotFound() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE, END_DATE);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CURRICULUM_ID);
    cm.setCurriculumStartDate(START_DATE);
    cm.setCurriculumEndDate(END_DATE);
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
  void shouldPatchCmDto() {
    CurriculumMembershipDTO dto = createDto(CURRICULUM_ID, PM_UUID, START_DATE, END_DATE);
    dto.setId(CURRICULUM_ID);

    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(CURRICULUM_ID);
    cm.setCurriculumStartDate(START_DATE);
    cm.setCurriculumEndDate(END_DATE);
    cm.setCurriculumId(CURRICULUM_ID);
    when(cmMapper.toEntity(dto)).thenReturn(cm);

    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setUuid(PM_UUID);
    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.of(pm));

    CurriculumMembership returnedCm = new CurriculumMembership();
    returnedCm.setId(CURRICULUM_ID);
    returnedCm.setCurriculumStartDate(START_DATE);
    returnedCm.setCurriculumEndDate(END_DATE);
    returnedCm.setCurriculumId(CURRICULUM_ID);
    returnedCm.setId(CM_ID);
    returnedCm.setProgrammeMembership(pm);

    CurriculumMembershipDTO returnedCmDto = createDto(CURRICULUM_ID, PM_UUID, START_DATE, END_DATE);
    returnedCmDto.setId(CM_ID);

    when(cmRepository.findById(dto.getId())).thenReturn(Optional.of(returnedCm));

    when(cmRepository.save(any(CurriculumMembership.class))).thenReturn(returnedCm);
    when(cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm)).thenReturn(
        returnedCmDto);

    CurriculumMembershipDTO result = cmService.patch(dto);
    assertEquals(returnedCmDto, result);
  }

  @Test
  void shouldReturnMessageWhenCurriculumMembershipNotFound() {
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setId(CM_ID);
    when(cmService.findOne(CM_ID)).thenReturn(null);

   CurriculumMembershipDTO result = cmService.patch(cmDto);

    assertEquals("Curriculum membership id not found.", result.getMessageList().get(0));
    verify(cmRepository, never()).save(any());
  }

  @Test
  void shouldReturnMessageWhenProgrammeMembershipNotFound() {
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setId(CM_ID);
    cmDto.setProgrammeMembershipUuid(PM_UUID);

    CurriculumMembershipDTO fromDb = new CurriculumMembershipDTO();
    when(cmService.findOne(CM_ID)).thenReturn(fromDb);
    when(pmRepository.findByUuid(PM_UUID)).thenReturn(Optional.empty());

    CurriculumMembershipDTO result = cmService.patch(cmDto);

    assertEquals("Programme membership id not found.", result.getMessageList().get(0));
    verify(cmRepository, never()).save(any());
  }
}
