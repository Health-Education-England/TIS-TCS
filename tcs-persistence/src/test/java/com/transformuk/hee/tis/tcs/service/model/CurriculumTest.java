package com.transformuk.hee.tis.tcs.service.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

class CurriculumTest {

  @Test
  void shouldReturnSameInstanceWhenChainingEligibleForPeriodOfGrace() {
    Curriculum curriculum = new Curriculum();

    Curriculum updatedCurriculum = curriculum.eligibleForPeriodOfGrace(true);

    assertThat("Unexpected curriculum.", updatedCurriculum, sameInstance(curriculum));
  }
}
