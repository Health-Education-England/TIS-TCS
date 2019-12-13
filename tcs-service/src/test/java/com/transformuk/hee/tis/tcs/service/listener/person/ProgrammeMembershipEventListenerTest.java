package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

//@RunWith(J.class)
public class ProgrammeMembershipEventListenerTest {

  private ProgrammeMembershipEventListener testClass = new ProgrammeMembershipEventListener();

  private ProgrammeMembershipDTO pastMembership;
  private ProgrammeMembershipDTO futureMembership;
  private ProgrammeMembershipDTO currentMembership;

  private LocalDate today = LocalDate.now();
  private LocalDate yesterday = today.minusDays(1);
  private LocalDate lastMonth = today.minusMonths(1);
  private LocalDate lastYear = today.minusYears(1);
  private LocalDate tomorrow = today.plusDays(1);
  private LocalDate nextMonth = today.plusMonths(1);
  private LocalDate nextYear = today.plusYears(1);

  public ProgrammeMembershipEventListenerTest() {
    pastMembership = new ProgrammeMembershipDTO();
    pastMembership.setProgrammeEndDate(yesterday);
    pastMembership.setProgrammeStartDate(lastYear);

    futureMembership = new ProgrammeMembershipDTO();
    futureMembership.setProgrammeEndDate(nextYear);
    futureMembership.setProgrammeStartDate(tomorrow);

    currentMembership = new ProgrammeMembershipDTO();
    currentMembership.setProgrammeEndDate(nextMonth);
    currentMembership.setProgrammeStartDate(lastMonth);
  }

  @Test
  public void testCalculateTrainingStatusWithNoProgrammes() {
    assertCalculatePersonRecordStatus(Status.INACTIVE, null);
    assertCalculatePersonRecordStatus(Status.INACTIVE,
        Collections.<ProgrammeMembershipDTO>emptyList());
  }

  @Test
  public void testCalculateTrainingStatusWithPastProgramme() {
    assertCalculatePersonRecordStatus(Status.INACTIVE, Collections.singletonList(pastMembership));
  }

  @Test
  public void testCalculateTrainingStatusWithFutureProgramme() {
    assertCalculatePersonRecordStatus(Status.INACTIVE, Collections.singletonList(futureMembership));
  }

  @Test
  public void testCalculateTrainingStatusWithCurrentProgramme() {
    assertCalculatePersonRecordStatus(Status.CURRENT, Collections.singletonList(currentMembership));
    assertCalculatePersonRecordStatus(Status.CURRENT,
        Arrays.asList(pastMembership, currentMembership, futureMembership));
  }

  private void assertCalculatePersonRecordStatus(Status expected,
      List<ProgrammeMembershipDTO> programmeMemberships) {
    Status actual = testClass.calculatePersonTrainingStatus(programmeMemberships);
    assertEquals(expected, actual);
  }
}
