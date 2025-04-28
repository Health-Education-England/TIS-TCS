package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
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
  public void setup() {
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
  public void tearDown() {
    entityManager.remove(associatedTrust1);
    entityManager.remove(associatedTrust2);
  }


  @Test
  @Transactional
  public void findPersonByIdShouldAlsoRetrieveAssociatedTrusts() {
    Optional<Post> result = postRepository.findPostWithTrustsById(postWithTrusts.getId());

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(postWithTrusts, result.get());
    Set<PostTrust> associatedTrusts = result.get().getAssociatedTrusts();
    Assert.assertTrue(associatedTrusts.contains(associatedTrust1));
    Assert.assertTrue(associatedTrusts.contains(associatedTrust2));
  }


  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/programme.sql", "/scripts/programmePost.sql"})
  @Sql(scripts = {"/scripts/deleteProgrammePost.sql", "/scripts/deletePosts.sql",
      "/scripts/deleteProgramme.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPostThatAreActiveOnly() {

    long programmeIdWithInactivePosts = 1L;
    List<Post> result = postRepository
        .findPostsForProgrammeIdAndNpnLike(programmeIdWithInactivePosts, StringUtils.EMPTY,
            Status.CURRENT);

    Assert.assertTrue(result.isEmpty());
  }

  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/programme.sql", "/scripts/programmePost.sql"})
  @Sql(scripts = {"/scripts/deleteProgrammePost.sql", "/scripts/deletePosts.sql",
      "/scripts/deleteProgramme.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsForProgrammeIdShouldReturnLinkedPost() {

    //there are 2 posts linked to programme 2, one is active, the other is inactive
    long programmeIdWithInactivePosts = 2L;
    List<Post> result = postRepository
        .findPostsForProgrammeIdAndNpnLike(programmeIdWithInactivePosts, StringUtils.EMPTY,
            Status.CURRENT);

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(new Long(2L), result.get(0).getId());
    Assert.assertEquals("XXX/XXX01/080/XXX/001", result.get(0).getNationalPostNumber());
    Assert.assertEquals(Status.CURRENT, result.get(0).getStatus());

  }

  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/placements.sql"})
  @Sql(scripts = {"/scripts/deletePlacements.sql",
      "/scripts/deletePosts.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

  public void findPostByPlacementHistoryIdShouldReturnPostLinkedToPlacement() {
    long placementId = 1L;
    Long expectedPostId = 1L;

    Optional<Post> result = postRepository.findPostByPlacementHistoryId(placementId);

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(expectedPostId, result.get().getId());
  }

  @Test
  @Sql(scripts = {"/scripts/posts.sql", "/scripts/placements.sql"})
  @Sql(scripts = {"/scripts/deletePlacements.sql",
      "/scripts/deletePosts.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostByPlacementHistoryIdShouldReturnEmptyOptionalIfNotPostExistsForPlacement() {
    long placementId = 9999L;

    Optional<Post> result = postRepository.findPostByPlacementHistoryId(placementId);

    Assert.assertFalse(result.isPresent());
  }

  @Transactional
  @Test
  @Sql(scripts = "/scripts/placementProgrammeSpecialty.sql")
  @Sql(scripts = "/scripts/deletePlacementProgrammeSpecialty.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPostsAndPlacementsByProgrammeIdAndSpecialtyIdShouldFindPostsLinkedToSpecialtyAndProgramme() {
    Long placementId1 = 3L, placementId2 = 30L;
    Long postId1 = 2L, postId2 = 20L;
    Long programmeId = 5L;
    Long specialtyId = 1L;
    Long traineeId1 = 4L, traineeId2 = 40L;
    String traineeForename1 = "John", traineeForename2 = "Joanne";

    Set<Post> results = postRepository
        .findPostsByProgrammeIdAndSpecialtyId(programmeId, specialtyId);

    Assert.assertNotNull(results);
    Assert.assertEquals(1, results.size());

    for (Post post : results) {
      Assert.assertTrue(postId1.equals(post.getId()));
      Assert.assertEquals(specialtyId,
          post.getSpecialties().iterator().next().getSpecialty().getId());
      Assert.assertEquals(programmeId, post.getProgrammes().iterator().next().getId());
      Assert.assertTrue(
          placementId1.equals(post.getPlacementHistory().iterator().next().getId()) || placementId2
              .equals(post.getPlacementHistory().iterator().next().getId()));
      Assert.assertTrue(
          traineeId1.equals(post.getPlacementHistory().iterator().next().getTrainee().getId())
              || traineeId2
              .equals(post.getPlacementHistory().iterator().next().getTrainee().getId()));
      Assert.assertTrue(traineeForename1.equals(
          post.getPlacementHistory().iterator().next().getTrainee().getContactDetails()
              .getForenames())
          || traineeForename2.equals(
          post.getPlacementHistory().iterator().next().getTrainee().getContactDetails()
              .getForenames()));

    }
  }

  @Test
  public void testClearOldPostReferencesInPostTableWhenDelete() {
    Post post1 = new Post();
    entityManager.persist(post1);

    Post post2 = new Post();
    post2.setOldPost(post1);
    entityManager.persist(post2);
    entityManager.flush();

    postRepository.clearOldPostReferences(post1.getId());
    entityManager.flush();
    entityManager.clear();

    Post updatedPost = entityManager.find(Post.class, post2.getId());
    Assert.assertNull(updatedPost.getOldPost());
  }

  @Test
  public void testClearNewPostReferencesInPostTableWhenDelete() {
    Post post1 = new Post();
    entityManager.persist(post1);

    Post post2 = new Post();
    post2.setNewPost(post1);
    entityManager.persist(post2);
    entityManager.flush();

    postRepository.clearNewPostReferences(post1.getId());
    entityManager.flush();
    entityManager.clear();

    Post updatedPost = entityManager.find(Post.class, post2.getId());
    assertNull(updatedPost.getNewPost());
  }

  @Test
  public void testClearPostReferencesForBothOldAndNewPostWhenDelete() {
    Post post1 = new Post();
    entityManager.persist(post1);

    Post post2 = new Post();
    post2.setOldPost(post1);
    post1.setNewPost(post2);
    entityManager.persist(post2);
    entityManager.flush();

    postRepository.clearPostReferences(post1.getId());
    entityManager.flush();
    entityManager.clear();

    Post updatedPost = entityManager.find(Post.class, post2.getId());
    assertNull(updatedPost.getOldPost());
    assertNull(updatedPost.getNewPost());
  }
}
