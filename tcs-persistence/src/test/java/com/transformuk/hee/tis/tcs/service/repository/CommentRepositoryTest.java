package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
public class CommentRepositoryTest {

  private static final Long PLACEMENT_ID_WITH_COMMENT = 3L;
  private static final Long NON_EXISTING_PLACEMENT_ID = 999L;
  private static final String EXPECTED_COMMENT_BODY = "2nd BODY OF COMMENT";

  @Autowired
  private CommentRepository commentRepository;

  @Sql(scripts = "/scripts/placementComment.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deletePlacementComment.sql")
  @Test
  public void findByPlacementIdShouldReturnLatestComment() {
    Optional<Comment> result = commentRepository
        .findFirstByPlacementIdOrderByAmendedDateDesc(PLACEMENT_ID_WITH_COMMENT);

    Assert.assertEquals(true, result.isPresent());
    Assert.assertEquals(EXPECTED_COMMENT_BODY, result.get().getBody());
  }

  @Sql(scripts = "/scripts/placementComment.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deletePlacementComment.sql")
  @Test
  public void findByPlacementIdShouldReturnEmptyOptionalWhenPlacementDoesntExist() {
    Optional<Comment> result = commentRepository
        .findFirstByPlacementIdOrderByAmendedDateDesc(NON_EXISTING_PLACEMENT_ID);

    Assert.assertEquals(false, result.isPresent());
  }
}
