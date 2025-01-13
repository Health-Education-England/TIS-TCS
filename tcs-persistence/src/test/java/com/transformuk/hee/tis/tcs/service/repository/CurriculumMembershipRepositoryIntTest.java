package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/scripts/curriculumMembership.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/deleteCurriculumMembership.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class CurriculumMembershipRepositoryIntTest {

  @Autowired
  private CurriculumMembershipRepository testObj;

  @Transactional
  @Test
  public void shouldFindByTraineeIdAndProgrammeId() {
    List<CurriculumMembership> result = testObj.findByTraineeIdAndProgrammeId(1L, 1L);

    Assert.assertEquals(2, result.size());
    Assert.assertEquals(result.get(0).getProgrammeMembership().getUuid(),
        result.get(1).getProgrammeMembership().getUuid());
    List<Long> resultIds = result.stream().map(r -> r.getId()).collect(Collectors.toList());
    Assert.assertTrue(resultIds.contains(1L));
    Assert.assertTrue(resultIds.contains(2L));
  }

  @Transactional
  @Test
  public void shouldFindByTraineeId() {
    List<CurriculumMembership> result = testObj.findByTraineeId(2L);

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(3L, result.get(0).getId().longValue());
  }

  @Transactional
  @Test
  public void shouldFindLatestCurriculumMembershipByTraineeId() {
    CurriculumMembership result = testObj.findLatestCurriculumMembershipByTraineeId(1L);
    Assert.assertEquals(1L, result.getId().longValue());
  }

  @Transactional
  @Test
  public void shouldFindAllCurriculumMembershipInDescOrderByTraineeId() {
    List<CurriculumMembership> result = testObj.findAllCurriculumMembershipInDescOrderByTraineeId(
        1L);
    Assert.assertEquals(3, result.size());
    LocalDate date1 = result.get(0).getProgrammeMembership().getProgrammeEndDate();
    LocalDate date2 = result.get(1).getProgrammeMembership().getProgrammeEndDate();
    LocalDate date3 = result.get(2).getProgrammeMembership().getProgrammeEndDate();

    Assert.assertFalse(date1.isBefore(date2));
    Assert.assertFalse(date2.isBefore(date3));
  }

  @Transactional
  @Test
  public void shouldFindByProgrammeId() {
    List<CurriculumMembership> result = testObj.findByProgrammeId(1L);
    Assert.assertEquals(3, result.size());
    List<Long> resultIds = result.stream().map(r -> r.getId()).collect(Collectors.toList());
    Assert.assertTrue(resultIds.contains(1L));
    Assert.assertTrue(resultIds.contains(2L));
    Assert.assertTrue(resultIds.contains(5L));
  }

  @Transactional
  @Test
  public void shouldFindLatestCurriculumByTraineeId() {
    CurriculumMembership result = testObj.findLatestCurriculumByTraineeId(1L);
    Assert.assertEquals(2L, result.getId().longValue());
  }

  @Transactional
  @Test
  public void shouldFindCmByCurriculumIdAndPmUuidAndDates() {
    List<CurriculumMembership> cmList = testObj.findByCurriculumIdAndPmUuidAndDates(2L,
        "004c4a2a-80fd-4312-83b4-5e8666db5166",
        LocalDate.of(2023, 8, 1), LocalDate.of(2027, 9, 4));
    Assert.assertEquals(1, cmList.size());
  }
}
