package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfigNonES;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import java.util.List;
import java.util.Set;

import lombok.var;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfigNonES.class)
public class PostEsrEventRepositoryTest {

  @Autowired
  private PostEsrEventRepository postEsrEventRepository;

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPostIdInShouldReturnEventsForAllProvidedPosts(){
    long firstPost = 1111111L;
    long secondPost = 2222222L;
    List<Long> postIds = Lists.newArrayList(firstPost, secondPost);

    Set<PostEsrEvent> result = postEsrEventRepository
        .findPostEsrEventByPostIdIn(postIds);

    Assert.assertNotNull(result);
    Assert.assertEquals(3, result.size());

    for (PostEsrEvent postEsrEvent : result) {
      Assert.assertTrue(postEsrEvent.getPost().getId().equals(firstPost) ||
          postEsrEvent.getPost().getId().equals(secondPost));
    }
  }

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPostIdInShouldReturnEmptyListWhenNoMatchingPostIdFound(){
    long nonExistingPostId = 9999L;
    List<Long> postIds = Lists.newArrayList(nonExistingPostId);

    Set<PostEsrEvent> result = postEsrEventRepository
        .findPostEsrEventByPostIdIn(postIds);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPositionNumberShouldReturnEmptyListWhenNoMatchingPositionNumberFound(){
    long nonExistingPostNumber = 9999L;

    Set<PostEsrEvent> result = postEsrEventRepository
        .findPostEsrEventsByPositionNumber(nonExistingPostNumber);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Sql(scripts = "/scripts/insertPostEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePostEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPostEsrEventByPositionNumberShouldReturnEvents(){
    long positionNumber = 2654522l;

    Set<PostEsrEvent> result = postEsrEventRepository
        .findPostEsrEventsByPositionNumber(positionNumber);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());
    var iter = result.iterator();
    Assert.assertTrue(iter.next().getPositionNumber().equals(positionNumber));
    Assert.assertTrue(iter.next().getPositionNumber().equals(positionNumber));
  }
}
