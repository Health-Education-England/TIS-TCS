package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.time.LocalDate;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CurriculumMembershipTest {
  private static final String LEAVING_REASON = "leaving reason";
  private static final String LEAVING_DESTINATION = "leaving destination";
  private static final String ROTATION_NAME = "rotation";
  private static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDate END_DATE = LocalDate.of(2020, 2, 1);

  private final CurriculumMembership curriculumMembership;

  public CurriculumMembershipTest() {
    Rotation rotation = new Rotation();
    rotation.setName(ROTATION_NAME);

    curriculumMembership = new CurriculumMembership();

    //The non-deprecated field get/setters are tested elsewhere; this just covers the deprecated ones
    curriculumMembership
        .programmeMembershipType(ProgrammeMembershipType.SUBSTANTIVE)
        .rotation(rotation)
        .programmeStartDate(START_DATE)
        .programmeEndDate(END_DATE)
        .leavingDestination(LEAVING_DESTINATION)
        .leavingReason(LEAVING_REASON);
  }

  @Test
  public void testDeprecatedProgrammeMembershipType() {
    assertThat(curriculumMembership.getProgrammeMembershipType()).isEqualTo(ProgrammeMembershipType.SUBSTANTIVE);
  }

  @Test
  public void testDeprecatedRotation() {
    assertThat(curriculumMembership.getRotation().getName()).isEqualTo(ROTATION_NAME);
  }

  @Test
  public void testDeprecatedProgrammeStartDate() {
    assertThat(curriculumMembership.getProgrammeStartDate()).isEqualTo(START_DATE);
  }

  @Test
  public void testDeprecatedProgrammeEndDate() {
    assertThat(curriculumMembership.getProgrammeEndDate()).isEqualTo(END_DATE);
  }

  @Test
  public void testDeprecatedLeavingDestination() {
    assertThat(curriculumMembership.getLeavingDestination()).isEqualTo(LEAVING_DESTINATION);
  }

  @Test
  public void testDeprecatedLeavingReason() {
    assertThat(curriculumMembership.getLeavingReason()).isEqualTo(LEAVING_REASON);
  }
}
