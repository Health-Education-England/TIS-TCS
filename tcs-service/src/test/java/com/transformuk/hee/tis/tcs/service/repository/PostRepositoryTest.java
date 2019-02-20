package com.transformuk.hee.tis.tcs.service.repository;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private EntityManager entityManager;

  private Post postWithTrusts, postWithNoTrusts;
  private PostTrust associatedTrust1, associatedTrust2;

  @Before
  @Transactional
  public void setup(){
    associatedTrust1 = new PostTrust();
    associatedTrust1.setTrustId(1111L);
    associatedTrust2 = new PostTrust();
    associatedTrust2.setTrustId(2222L);

    postWithTrusts = new Post();
    postWithTrusts.setAssociatedTrusts(Sets.newHashSet(associatedTrust1, associatedTrust2));
    entityManager.persist(postWithTrusts);

    postWithNoTrusts = new Post();
    entityManager.persist(postWithNoTrusts);
  }


  @After
  @Transactional
  public void tearDown(){
    entityManager.remove(postWithTrusts);
    entityManager.remove(associatedTrust1);
    entityManager.remove(associatedTrust2);
  }


  @Test
  @Transactional
  public void findPersonByIdShouldAlsoRetrieveAssociatedTrusts(){
    Optional<Post> result = postRepository.findPostWithTrustsById(postWithTrusts.getId());

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(postWithTrusts, result.get());
    Set<PostTrust> associatedTrusts = result.get().getAssociatedTrusts();
    Assert.assertTrue(associatedTrusts.contains(associatedTrust1));
    Assert.assertTrue(associatedTrusts.contains(associatedTrust2));
  }


  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/programme.sql", "/scripts/programmePost.sql"})
  @Sql(scripts = {"/scripts/deleteProgrammePost.sql", "/scripts/deletePosts.sql", "/scripts/deleteProgramme.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPostThatAreActiveOnly() {

    long programmeIdWithInactivePosts = 1L;
    List<Post> result = postRepository.findPostsForProgrammeIdAndNpnLike(programmeIdWithInactivePosts, StringUtils.EMPTY, Status.CURRENT);

    Assert.assertTrue(result.isEmpty());
  }

  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/programme.sql", "/scripts/programmePost.sql"})
  @Sql(scripts = {"/scripts/deleteProgrammePost.sql", "/scripts/deletePosts.sql", "/scripts/deleteProgramme.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPost() {

    //there are 2 posts linked to programme 2, one is active, the other is inactive
    long programmeIdWithInactivePosts = 2L;
    List<Post> result = postRepository.findPostsForProgrammeIdAndNpnLike(programmeIdWithInactivePosts, StringUtils.EMPTY, Status.CURRENT);

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(new Long(2L), result.get(0).getId());
    Assert.assertEquals("XXX/XXX01/080/XXX/001", result.get(0).getNationalPostNumber());
    Assert.assertEquals(Status.CURRENT, result.get(0).getStatus());

  }
}