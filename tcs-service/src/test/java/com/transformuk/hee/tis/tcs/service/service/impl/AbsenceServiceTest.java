package com.transformuk.hee.tis.tcs.service.service.impl;


import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
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
import com.transformuk.hee.tis.tcs.service.service.mapper.AbsenceMapperImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbsenceServiceTest {

  public static final Long NON_EXISTING_ABSENCE_ID = 1111L;
  public static final Long ABSENCE_ID = 8888L;
  public static final String ESR_ABSENCE_ID = "1208";
  public static final String NON_EXISTING_ESR_ABSENCE_ID = "sdfsdfsd";
  public static final Long PERSON_ID = 5555L;
  public static final Integer ABSENCE_ID_INT = 1;

  @InjectMocks
  private AbsenceService testObj;

  @Spy
  private final AbsenceMapper absenceMapper = new AbsenceMapperImpl();
  @Mock
  private AbsenceRepository absenceRepositoryMock;
  @Mock
  private PersonRepository personRepositoryMock;
  @Captor
  private ArgumentCaptor<Absence> absenceArgumentCaptor;
  @Mock
  EntityManager entityManager;

  @Test(expected = IllegalArgumentException.class)
  public void findByIdShouldThrowExceptionWhenIdIsNull() {
    try {
      testObj.findById(null);
    } finally {
      Mockito.verifyZeroInteractions(absenceRepositoryMock);
      Mockito.verifyZeroInteractions(absenceMapper);
    }
  }

  @Test
  public void findByIdShouldReturnEmptyOptionalWhenItDoesntExist() {
    when(absenceRepositoryMock.findById(NON_EXISTING_ABSENCE_ID)).thenReturn(Optional.empty());

    Optional<AbsenceDTO> result = testObj.findById(NON_EXISTING_ABSENCE_ID);

    Assert.assertFalse(result.isPresent());
    verifyZeroInteractions(absenceMapper);
  }

  @Test
  public void findByIdShouldReturnFilledOptional() {
    Absence absence = new Absence();
    absence.setId(ABSENCE_ID);

    when(absenceRepositoryMock.findById(NON_EXISTING_ABSENCE_ID)).thenReturn(Optional.of(absence));

    Optional<AbsenceDTO> result = testObj.findById(NON_EXISTING_ABSENCE_ID);

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(ABSENCE_ID, result.get().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findByAbsenceIdShouldThrowExceptionWhenIdIsNull() {
    try {
      testObj.findAbsenceByAbsenceAttendanceId(null);
    } finally {
      Mockito.verifyZeroInteractions(absenceRepositoryMock);
      Mockito.verifyZeroInteractions(absenceMapper);
    }
  }

  @Test
  public void findByAbsenceIdShouldReturnEmptyOptionalWhenItDoesntExist() {
    when(absenceRepositoryMock.findByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID))
        .thenReturn(Optional.empty());

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID);

    Assert.assertFalse(result.isPresent());
    verifyZeroInteractions(absenceMapper);
  }

  @Test
  public void findByAbsenceIdShouldReturnFilledOptional() {
    Absence absence = new Absence();
    absence.setId(ABSENCE_ID);

    when(absenceRepositoryMock.findByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID))
        .thenReturn(Optional.of(absence));

    Optional<AbsenceDTO> result = testObj.findAbsenceByAbsenceAttendanceId(NON_EXISTING_ESR_ABSENCE_ID);

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(absence.getId(), result.get().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAbsenceShouldThrowExceptionWhenAbsenceIsNull() {
    try {
      testObj.createAbsence(null);
    } finally {
      verifyZeroInteractions(absenceMapper);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAbsenceShouldThrowExceptionWhenAbsenceIdIsNotNull() {
    try {
      AbsenceDTO absenceDto = new AbsenceDTO();
      absenceDto.setId(ABSENCE_ID);

      testObj.createAbsence(absenceDto);
    } finally {
      verifyZeroInteractions(absenceMapper);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test
  public void createAbsenceShouldSaveAbsence() {
    AbsenceDTO absenceDto = new AbsenceDTO();
    absenceDto.setPersonId(PERSON_ID);

    Absence absence = new Absence();
    absence.setId(ABSENCE_ID);
    Person person = new Person();
    person.setId(PERSON_ID);
    absence.setPerson(person);

    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));

    ArgumentCaptor<Absence> absenceCaptor = ArgumentCaptor.forClass(Absence.class);
    when(absenceRepositoryMock.saveAndFlush(absenceCaptor.capture())).thenReturn(absence);

    AbsenceDTO result = testObj.createAbsence(absenceDto);

    Assert.assertEquals(ABSENCE_ID, result.getId());
    Assert.assertEquals(PERSON_ID, result.getPersonId());

    Absence savedAbsence = absenceCaptor.getValue();
    Assert.assertEquals(person, savedAbsence.getPerson());

    verify(personRepositoryMock).findById(PERSON_ID);
    verify(absenceRepositoryMock).saveAndFlush(any());
  }


  @Test(expected = IllegalArgumentException.class)
  public void updateAbsenceShouldThrowExceptionWhenAbsenceIsNull() {
    try {
      testObj.updateAbsence(null);
    } finally {
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateAbsenceShouldThrowExceptionWhenAbsenceIdIsNull() {
    try {
      testObj.updateAbsence(new AbsenceDTO());
    } finally {
      verifyZeroInteractions(absenceMapper);
      verifyZeroInteractions(personRepositoryMock);
      verifyZeroInteractions(absenceRepositoryMock);
    }
  }

  @Test
  public void updateAbsenceShouldSaveAbsence() {
    AbsenceDTO absenceDto = new AbsenceDTO();
    absenceDto.setId(ABSENCE_ID);
    absenceDto.setPersonId(PERSON_ID);

    Absence absence = new Absence();
    absence.setId(ABSENCE_ID);
    Person person = new Person();
    person.setId(PERSON_ID);
    absence.setPerson(person);

    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));

    ArgumentCaptor<Absence> absenceCaptor = ArgumentCaptor.forClass(Absence.class);
    when(absenceRepositoryMock.saveAndFlush(absenceCaptor.capture())).thenReturn(absence);


    AbsenceDTO result = testObj.updateAbsence(absenceDto);

    Assert.assertEquals(ABSENCE_ID, result.getId());
    Assert.assertEquals(PERSON_ID, result.getPersonId());

    Absence savedAbsence = absenceCaptor.getValue();
    Assert.assertEquals(ABSENCE_ID, savedAbsence.getId());
    Assert.assertEquals(person, savedAbsence.getPerson());

    verify(personRepositoryMock).findById(PERSON_ID);
    verify(absenceRepositoryMock).saveAndFlush(any());
  }

  @Test(expected = IllegalArgumentException.class)
  public void patchAbsenceShouldThrowExceptionWhenAbsenceIsNull() throws Exception {
    try {
      testObj.patchAbsence(null);
    } finally {
      verifyZeroInteractions(absenceRepositoryMock);
      verifyZeroInteractions(absenceMapper);
    }
  }

  @Test
  public void patchAbsenceShouldUpdateOnlyFieldsProvidedInMap() throws Exception {
    Integer newDuration = new Integer(30);
    String newEndDateString = "2000-02-01";
    String newAmendedDateString = "2000-02-09T09:00:00.000";
    LocalDate newEndDate = LocalDate.parse(newEndDateString);
    LocalDateTime newAmendedDate = LocalDateTime.parse(newAmendedDateString);

    Map<String, Object> params = Maps.newHashMap();
    params.put("id", ABSENCE_ID_INT);
    params.put("endDate", newEndDateString);
    params.put("durationInDays", newDuration);
    params.put("amendedDate", newAmendedDateString);

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
    absenceStub.setAmendedDate(LocalDateTime.of(2000, 1, 1, 9, 0));
    absenceStub.setPerson(new Person());

    when(absenceRepositoryMock.findById(1L)).thenReturn(Optional.of(absenceStub));
    when(absenceRepositoryMock.saveAndFlush(absenceArgumentCaptor.capture())).then(returnsFirstArg());

    Optional<AbsenceDTO> result = testObj.patchAbsence(params);

    Assertions.assertTrue(result.isPresent());

    Absence capturedModifiedAbsence = absenceArgumentCaptor.getValue();
    Assertions.assertEquals(new Long(ABSENCE_ID_INT), capturedModifiedAbsence.getId());
    Assertions.assertEquals(originalStartDate, capturedModifiedAbsence.getStartDate());
    Assertions.assertEquals(newEndDate, capturedModifiedAbsence.getEndDate());
    Assertions.assertEquals(new Long(newDuration), capturedModifiedAbsence.getDurationInDays());
    Assertions.assertEquals(originalAttendanceId, capturedModifiedAbsence.getAbsenceAttendanceId());
    Assertions.assertEquals(newAmendedDate, capturedModifiedAbsence.getAmendedDate());
  }
}
