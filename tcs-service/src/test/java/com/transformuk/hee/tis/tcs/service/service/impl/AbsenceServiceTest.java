package com.transformuk.hee.tis.tcs.service.service.impl;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.AbsenceRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.AbsenceMapper;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbsenceServiceTest {

  public static final long NON_EXISTING_ABSENCE_ID = 1111L;
  public static final long ABSENCE_ID = 8888L;
  public static final String ESR_ABSENCE_ID = "1208";
  public static final String NON_EXISTING_ESR_ABSENCE_ID = "sdfsdfsd";
  public static final long PERSON_ID = 5555L;
  public static final Integer ABSENCE_ID_INT = new Integer(1);

  @InjectMocks
  private AbsenceService testObj;

  @Mock
  private AbsenceMapper absenceMapperMock;
  @Mock
  private AbsenceRepository absenceRepositoryMock;
  @Mock
  private PersonRepository personRepositoryMock;
  @Mock
  private Absence absenceMock, savedAbsenceMock;
  @Mock
  private AbsenceDTO absenceDTOMock, savedAbsenceDtoMock;
  @Mock
  private Person personMock;
  @Captor
  private ArgumentCaptor<Absence> absenceArgumentCaptor;

  @Test(expected = IllegalArgumentException.class)
  public void findByIdShouldThrowExceptionWhenIdIsNull() {
    try {
      testObj.findById(null);
    } finally {
      Mockito.verifyZeroInteractions(absenceRepositoryMock);
      Mockito.verifyZeroInteractions(absenceMapperMock);
    }
  }

  @Test
  public void findByIdShouldReturnEmptyOptionalWhenItDoesntExist() {
    when(absenceRepositoryMock.findById(NON_EXISTING_ABSENCE_ID)).thenReturn(Optional.empty());

    Optional<AbsenceDTO> result = testObj.findById(NON_EXISTING_ABSENCE_ID);

    Assert.assertFalse(result.isPresent());
    verifyZeroInteractions(absenceMapperMock);
  }

  @Test
  public void findByIdShouldReturnFilledOptional() {
    when(absenceRepositoryMock.findById(NON_EXISTING_ABSENCE_ID))
        .thenReturn(Optional.of(absenceMock));
    when(absenceMapperMock.toDto(absenceMock)).thenReturn(absenceDTOMock);

    Optional<AbsenceDTO> result = testObj.findById(NON_EXISTING_ABSENCE_ID);

    Assert.assertTrue(result.isPresent());
    Assert.assertSame(absenceDTOMock, result.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findByAbsenceIdShouldThrowExceptionWhenIdIsNull() {
    try {
      testObj.findAbsenceByAbsenceAttendanceId(null);
    } finally {
      Mockito.verifyZeroInteractions(absenceRepositoryMock);
      Mockito.verifyZeroInteractions(absenceMapperMock);
    }
  }

  @Test
  public void findByAbsenceIdShouldReturnEmptyOptionalWhenItDoesntExist() {
    when(absenceRepositoryMock.findByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID))
        .thenReturn(Optional.empty());

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID);

    Assert.assertFalse(result.isPresent());
    verifyZeroInteractions(absenceMapperMock);
  }

  @Test
  public void findByAbsenceIdShouldReturnFilledOptional() {
    when(absenceRepositoryMock.findByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID))
        .thenReturn(Optional.of(absenceMock));
    when(absenceMapperMock.toDto(absenceMock)).thenReturn(absenceDTOMock);

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID);

    Assert.assertTrue(result.isPresent());
    Assert.assertSame(absenceDTOMock, result.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAbsenceShouldThrowExceptionWhenAbsenceIsNull() {
    try {
      testObj.createAbsence(null);
    } finally {
      verifyZeroInteractions(absenceMapperMock);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAbsenceShouldThrowExceptionWhenAbsenceIdIsNotNull() {
    try {
      when(absenceDTOMock.getId()).thenReturn(ABSENCE_ID);
      testObj.createAbsence(absenceDTOMock);
    } finally {
      verifyZeroInteractions(absenceMapperMock);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test
  public void createAbsenceShouldSaveAbsence() {
    when(absenceDTOMock.getId()).thenReturn(null);
    when(absenceMapperMock.toEntity(absenceDTOMock)).thenReturn(absenceMock);
    when(absenceDTOMock.getPersonId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(personMock));
    when(absenceRepositoryMock.saveAndFlush(absenceMock)).thenReturn(savedAbsenceMock);
    when(absenceMapperMock.toDto(savedAbsenceMock)).thenReturn(savedAbsenceDtoMock);

    AbsenceDTO result = testObj.createAbsence(absenceDTOMock);

    Assert.assertSame(savedAbsenceDtoMock, result);

    verify(absenceMapperMock).toEntity(absenceDTOMock);
    verify(personRepositoryMock).findById(PERSON_ID);
    verify(absenceMock).setPerson(personMock);
    verify(absenceRepositoryMock).saveAndFlush(absenceMock);
    verify(absenceMapperMock).toDto(savedAbsenceMock);
  }


  @Test(expected = IllegalArgumentException.class)
  public void updateAbsenceShouldThrowExceptionWhenAbsenceIsNull() {
    try {
      testObj.createAbsence(null);
    } finally {
      verifyZeroInteractions(absenceMapperMock);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateAbsenceShouldThrowExceptionWhenAbsenceIdIsNull() {
    try {
      testObj.createAbsence(absenceDTOMock);
    } finally {
      verifyZeroInteractions(absenceMapperMock);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test
  public void updateAbsenceShouldSaveAbsence() {
    when(absenceDTOMock.getId()).thenReturn(ABSENCE_ID);
    when(absenceMapperMock.toEntity(absenceDTOMock)).thenReturn(absenceMock);
    when(absenceDTOMock.getPersonId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(personMock));
    when(absenceRepositoryMock.saveAndFlush(absenceMock)).thenReturn(savedAbsenceMock);
    when(absenceMapperMock.toDto(savedAbsenceMock)).thenReturn(savedAbsenceDtoMock);

    AbsenceDTO result = testObj.updateAbsence(absenceDTOMock);

    Assert.assertSame(savedAbsenceDtoMock, result);

    verify(absenceMapperMock).toEntity(absenceDTOMock);
    verify(personRepositoryMock).findById(PERSON_ID);
    verify(absenceMock).setPerson(personMock);
    verify(absenceRepositoryMock).saveAndFlush(absenceMock);
    verify(absenceMapperMock).toDto(savedAbsenceMock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void patchAbsenceShouldThrowExceptionWhenAbsenceIsNull() throws Exception {
    try {
      testObj.patchAbsence(null);
    } finally {
      verifyZeroInteractions(absenceRepositoryMock);
      verifyZeroInteractions(absenceMapperMock);
    }
  }

  @Test
  public void patchAbsenceShouldUpdateOnlyFieldsProvidedInMap() throws Exception {
    Integer newDuration = new Integer(30);
    String newEndDateString = "2000-02-01";
    LocalDate newEndDate = LocalDate.parse(newEndDateString);

    Map<String, Object> params = Maps.newHashMap();
    params.put("id", ABSENCE_ID_INT);
    params.put("endDate", newEndDateString);
    params.put("durationInDays", newDuration);

    LocalDate originalStartDate = LocalDate.of(2000, 1, 1);
    LocalDate originalEndDate = null;
    Long originalDuration = 0L;
    String originalAttendanceId = "11111";

    Absence absenceStub = new Absence();
    absenceStub.setId(new Long(ABSENCE_ID_INT));
    absenceStub.setStartDate(originalStartDate);
    absenceStub.setEndDate(originalEndDate);
    absenceStub.setDurationInDays(originalDuration);
    absenceStub.setAbsenceAttendanceId(originalAttendanceId);
    absenceStub.setPerson(new Person());

    when(absenceRepositoryMock.findById(1L)).thenReturn(Optional.of(absenceStub));
    when(absenceRepositoryMock.saveAndFlush(absenceArgumentCaptor.capture())).thenReturn(absenceMock);
    when(absenceMapperMock.toDto(absenceMock)).thenReturn(absenceDTOMock);

    Optional<AbsenceDTO> result = testObj.patchAbsence(params);

    Assertions.assertTrue(result.isPresent());

    Absence capturedModifiedAbsence = absenceArgumentCaptor.getValue();
    Assertions.assertEquals(new Long(ABSENCE_ID_INT), capturedModifiedAbsence.getId());
    Assertions.assertEquals(originalStartDate, capturedModifiedAbsence.getStartDate());
    Assertions.assertEquals(newEndDate, capturedModifiedAbsence.getEndDate());
    Assertions.assertEquals(new Long(newDuration), capturedModifiedAbsence.getDurationInDays());
    Assertions.assertEquals(originalAttendanceId, capturedModifiedAbsence.getAbsenceAttendanceId());
  }


}
