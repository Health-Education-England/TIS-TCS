package com.transformuk.hee.tis.tcs.service.model;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

public class PersonTest extends Person {

  private ProgrammeMembership pastMembership;
  private ProgrammeMembership futureMembership;
  private ProgrammeMembership currentMembership;

  private LocalDate today = LocalDate.now();
  private LocalDate yesterday = today.minusDays(1);
  private LocalDate lastMonth = today.minusMonths(1);
  private LocalDate lastYear = today.minusYears(1);
  private LocalDate tomorrow = today.plusDays(1);
  private LocalDate nextMonth = today.plusMonths(1);
  private LocalDate nextYear = today.plusYears(1);

  public PersonTest() {

    pastMembership = new ProgrammeMembership();
    pastMembership.setProgrammeEndDate(yesterday);
    pastMembership.setProgrammeStartDate(lastYear);

    futureMembership = new ProgrammeMembership();
    futureMembership.setProgrammeEndDate(nextYear);
    futureMembership.setProgrammeStartDate(tomorrow);

    currentMembership = new ProgrammeMembership();
    currentMembership.setProgrammeEndDate(nextMonth);
    currentMembership.setProgrammeStartDate(lastMonth);
  }

  @Test
  public void testCalculateTrainingStatusWithNoProgrammes() {
    assertPersonRecordStatusEquals(Status.INACTIVE, null);
    assertPersonRecordStatusEquals(Status.INACTIVE,
        Collections.emptySet());
  }

  @Test
  public void testCalculateTrainingStatusWithPastProgramme() {
    assertPersonRecordStatusEquals(Status.INACTIVE, Collections.singleton(pastMembership));
  }

  @Test
  public void testCalculateTrainingStatusWithFutureProgramme() {
    assertPersonRecordStatusEquals(Status.INACTIVE, Collections.singleton(futureMembership));
  }

  @Test
  public void testCalculateTrainingStatusWithCurrentProgramme() {
    assertPersonRecordStatusEquals(Status.CURRENT, Collections.singleton(currentMembership));
    Set<ProgrammeMembership> allMemberships = new HashSet<>();
    allMemberships.addAll(Arrays.asList(pastMembership, currentMembership, futureMembership));
    assertPersonRecordStatusEquals(Status.CURRENT, allMemberships);
  }

  private void assertPersonRecordStatusEquals(Status expected,
      Set<ProgrammeMembership> programmeMemberships) {
    Person person = new Person().programmeMemberships(programmeMemberships);
    Status actual = person.programmeMembershipsStatus();
    assertEquals(expected, actual);
  }
}
