package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.Assert.*;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Post;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
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
  @Sql(scripts = {"/scripts/deleteAbsence.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPostThatAreActiveOnly() {

    Optional<Absence> result = absenceRepository.findByAbsenceAttendanceId("11122");

    Assert.assertTrue(result.isPresent());
    Absence absence = result.get();
    Assert.assertEquals(absence.getId(), new Long(2));
    Assert.assertEquals(absence.getPerson(), new Long(1));
    Assert.assertEquals(absence.getDurationInDays(), new Long(30L));
//    Assert.assertEquals(absence.getId());
//    Assert.assertEquals(absence.getId());
  }

}
