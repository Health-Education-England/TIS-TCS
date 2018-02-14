package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.Post;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostRepositoryIntTest {

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private EntityManager em;

  private Post post1, post2, post3;

  @Before
  public void setup(){
    post1 = new Post();
    post1.setNationalPostNumber("NTH/RTD01/007/STR/001");
    em.persist(post1);
    post2 = new Post();
    post2.setNationalPostNumber("NTH/RTD01/007/STR/001/M");
    em.persist(post2);
    post3 = new Post();
    post3.setNationalPostNumber("SOUTH/RTD01/007/STR/001/M");
    em.persist(post3);
  }

  @Transactional
  @Test
  public void findByNationalPostNumberStartingWithShouldReturnPostsWithSimilarPostNumbers(){
    Set<Post> byNationalPostNumber = postRepository.findByNationalPostNumberStartingWith("NTH/RTD01/007/STR");
    Assert.assertEquals(2, byNationalPostNumber.size());
  }

}