package com.transformuk.hee.tis.tcs.service.model;

import java.time.LocalDate;

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
  }
}