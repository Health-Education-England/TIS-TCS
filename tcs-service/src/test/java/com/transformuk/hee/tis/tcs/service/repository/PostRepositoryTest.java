package com.transformuk.hee.tis.tcs.service.repository;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
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
}