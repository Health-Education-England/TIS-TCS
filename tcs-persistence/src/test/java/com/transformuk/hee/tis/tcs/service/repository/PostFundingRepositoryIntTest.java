package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = "/scripts/postFundings.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/deletePostFundings.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class PostFundingRepositoryIntTest {

  @Autowired
  private PostFundingRepository testObj;

  @Transactional
  @Test
  void shouldCountCurrentFundings() {
    long result = testObj.countCurrentFundings(111L);
    assertEquals(0, result);

    result = testObj.countCurrentFundings(222L);
    assertEquals(1, result);

    result = testObj.countCurrentFundings(333L);
    assertEquals(0, result);

    result = testObj.countCurrentFundings(444L);
    assertEquals(1, result);

    result = testObj.countCurrentFundings(555L);
    assertEquals(1, result);

    result = testObj.countCurrentFundings(666L);
    assertEquals(1, result);

    result = testObj.countCurrentFundings(777L);
    assertEquals(0, result);

    result = testObj.countCurrentFundings(888L);
    assertEquals(4, result);
  }
}
