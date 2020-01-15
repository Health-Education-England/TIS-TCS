package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Transactional
public class AbsenceRepositoryTest {

  @Autowired
  private AbsenceRepository absenceRepository;

  @Test
  @Sql(scripts = {"/scripts/absence.sql"})
  @Sql(scripts = {
      "/scripts/deleteAbsence.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPostThatAreActiveOnly() {

    Optional<Absence> result = absenceRepository.findByAbsenceAttendanceId("11122");

    Assert.assertTrue(result.isPresent());
    Absence absence = result.get();
    Assert.assertEquals(absence.getId(), new Long(2));
    Assert.assertEquals(absence.getPerson().getId(), new Long(1));
    Assert.assertEquals(absence.getDurationInDays(), new Long(30L));
    LocalDate startDate = absence.getStartDate();
    Assert.assertEquals(startDate.getYear(), 2020);
    Assert.assertEquals(startDate.getMonthValue(), 1);
    Assert.assertEquals(startDate.getDayOfMonth(), 1);
    LocalDate endDate = absence.getEndDate();
    Assert.assertEquals(endDate.getYear(), 2021);
    Assert.assertEquals(endDate.getMonthValue(), 2);
    Assert.assertEquals(endDate.getDayOfMonth(), 3);
  }

}
