package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfigNonES;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfigNonES.class)
public class PostEsrLatestEventViewRepositoryTest {

  @Autowired
  private PostEsrLatestEventViewRepository repository;

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPostIdInShouldReturnEventsForAllProvidedPosts(){
    long postId = 1111111L;

    List<PostEsrLatestEventView> aaa = repository.findAll();

    Set<PostEsrLatestEventView> result = repository.findPostEsrLatestEventByPostId(postId);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
  }

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPostIdInShouldReturnEmptyListWhenNoMatchingPostIdFound(){
    long nonExistingPostId = 9999L;

    Set<PostEsrLatestEventView> result = repository.findPostEsrLatestEventByPostId(nonExistingPostId);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

}
