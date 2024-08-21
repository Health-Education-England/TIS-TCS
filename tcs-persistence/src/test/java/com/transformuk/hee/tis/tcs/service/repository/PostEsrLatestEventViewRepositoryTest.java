package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import com.transformuk.hee.tis.tcs.service.TestConfigNonES;
import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(classes = TestConfigNonES.class)
class PostEsrLatestEventViewRepositoryTest {

  @Autowired
  private PostEsrLatestEventViewRepository repository;

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  void findPostEsrEventByPostIdInShouldReturnEventsForAllProvidedPosts() {
    long postId = 1111111L;

    Set<PostEsrLatestEventView> result = repository.findPostEsrLatestEventByPostIdAndStatus(
        postId, PostEsrEventStatus.RECONCILED);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  void findPostEsrEventByPostIdInShouldReturnEmptyListWhenNoMatchingPostIdFound() {
    long nonExistingPostId = 9999L;

    Set<PostEsrLatestEventView> result = repository.findPostEsrLatestEventByPostIdAndStatus(
        nonExistingPostId, PostEsrEventStatus.RECONCILED);

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
