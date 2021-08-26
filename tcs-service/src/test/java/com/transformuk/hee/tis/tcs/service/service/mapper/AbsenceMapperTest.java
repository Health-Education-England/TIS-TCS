package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsenceMapperTest {

  private AbsenceMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new AbsenceMapperImpl();
  }

  @Test
  void shouldPatchAbsenceAttendanceIdWhenNewValue() {
    Absence absence = new Absence();
    absence.setAbsenceAttendanceId("initial");

    Map<String, Object> patchData = Collections.singletonMap("absenceAttendanceId", "patched");

    mapper.patch(patchData, absence);

    assertThat("Unexpected absence attendance ID.", absence.getAbsenceAttendanceId(),
        is("patched"));
  }

  @Test
  void shouldPatchAbsenceAttendanceIdWhenNullValue() {
    Absence absence = new Absence();
    absence.setAbsenceAttendanceId("initial");

    Map<String, Object> patchData = Collections.singletonMap("absenceAttendanceId", null);

    mapper.patch(patchData, absence);

    assertThat("Unexpected absence attendance ID.", absence.getAbsenceAttendanceId(), nullValue());
  }

  @Test
  void shouldNotPatchAbsenceAttendanceIdWhenNoValue() {
    Absence absence = new Absence();
    absence.setAbsenceAttendanceId("initial");

    Map<String, Object> patchData = Collections.emptyMap();

    mapper.patch(patchData, absence);

    assertThat("Unexpected absence attendance ID.", absence.getAbsenceAttendanceId(),
        is("initial"));
  }

  @Test
  void shouldPatchDurationInDaysWhenNewValue() {
    Absence absence = new Absence();
    absence.setDurationInDays(1L);

    Map<String, Object> patchData = Collections.singletonMap("durationInDays", 2);

    mapper.patch(patchData, absence);

    assertThat("Unexpected duration in days.", absence.getDurationInDays(), is(2L));
  }

  @Test
  void shouldPatchDurationInDaysWhenNullValue() {
    Absence absence = new Absence();
    absence.setDurationInDays(1L);

    Map<String, Object> patchData = Collections.singletonMap("durationInDays", null);

    mapper.patch(patchData, absence);

    assertThat("Unexpected duration in days.", absence.getDurationInDays(), nullValue());
  }

  @Test
  void shouldNotPatchDurationInDaysWhenNoValue() {
    Absence absence = new Absence();
    absence.setDurationInDays(1L);

    Map<String, Object> patchData = Collections.emptyMap();

    mapper.patch(patchData, absence);

    assertThat("Unexpected duration in days.", absence.getDurationInDays(), is(1L));
  }

  @Test
  void shouldPatchStartDateWhenNewValue() {
    Absence absence = new Absence();
    absence.setStartDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.singletonMap("startDate", LocalDate.MAX.toString());

    mapper.patch(patchData, absence);

    assertThat("Unexpected start date.", absence.getStartDate(), is(LocalDate.MAX));
  }

  @Test
  void shouldPatchStartDateWhenNullValue() {
    Absence absence = new Absence();
    absence.setStartDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.singletonMap("startDate", null);

    mapper.patch(patchData, absence);

    assertThat("Unexpected start date.", absence.getStartDate(), nullValue());
  }

  @Test
  void shouldNotPatchStartDateWhenNoValue() {
    Absence absence = new Absence();
    absence.setStartDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.emptyMap();

    mapper.patch(patchData, absence);

    assertThat("Unexpected start date.", absence.getStartDate(), is(LocalDate.MIN));
  }

  @Test
  void shouldPatchEndDateWhenNewValue() {
    Absence absence = new Absence();
    absence.setEndDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.singletonMap("endDate", LocalDate.MAX.toString());

    mapper.patch(patchData, absence);

    assertThat("Unexpected end date.", absence.getEndDate(), is(LocalDate.MAX));
  }

  @Test
  void shouldPatchEndDateWhenNullValue() {
    Absence absence = new Absence();
    absence.setEndDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.singletonMap("endDate", null);

    mapper.patch(patchData, absence);

    assertThat("Unexpected end date.", absence.getEndDate(), nullValue());
  }

  @Test
  void shouldNotPatchEndDateWhenNoValue() {
    Absence absence = new Absence();
    absence.setEndDate(LocalDate.MIN);

    Map<String, Object> patchData = Collections.emptyMap();

    mapper.patch(patchData, absence);

    assertThat("Unexpected end date.", absence.getEndDate(), is(LocalDate.MIN));
  }

  @Test
  void shouldPatchAmendedDateWhenNewValue() {
    Absence absence = new Absence();
    absence.setAmendedDate(LocalDateTime.MIN);

    Map<String, Object> patchData = Collections
        .singletonMap("amendedDate", LocalDateTime.MAX.toString());

    mapper.patch(patchData, absence);

    assertThat("Unexpected amended date.", absence.getAmendedDate(), is(LocalDateTime.MAX));
  }

  @Test
  void shouldPatchAmendedDateWhenNullValue() {
    Absence absence = new Absence();
    absence.setAmendedDate(LocalDateTime.MIN);

    Map<String, Object> patchData = Collections.singletonMap("amendedDate", null);

    mapper.patch(patchData, absence);

    assertThat("Unexpected amended date.", absence.getAmendedDate(), nullValue());
  }

  @Test
  void shouldNotPatchAmendedDateWhenNoValue() {
    Absence absence = new Absence();
    absence.setAmendedDate(LocalDateTime.MIN);

    Map<String, Object> patchData = Collections.emptyMap();

    mapper.patch(patchData, absence);

    assertThat("Unexpected amended date.", absence.getAmendedDate(), is(LocalDateTime.MIN));
  }

  @Test
  void shouldNotPatchId() {
    Absence absence = new Absence();
    absence.setId(1L);

    Map<String, Object> patchData = Collections.singletonMap("id", 2L);

    mapper.patch(patchData, absence);

    assertThat("Unexpected id.", absence.getId(), is(1L));
  }

  @Test
  void shouldNotPatchPerson() {
    Absence absence = new Absence();
    Person person = new Person();
    person.setId(1L);
    absence.setPerson(person);

    Map<String, Object> patchData = Collections.singletonMap("personId", 2L);

    mapper.patch(patchData, absence);

    assertThat("Unexpected person id.", absence.getPerson().getId(), is(1L));
  }
}
