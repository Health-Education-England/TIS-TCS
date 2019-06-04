package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlacementRepositoryTest {

  @Autowired
  private PlacementRepository testObj;

  @Transactional
  @Test
  @Sql(scripts = "/scripts/placementProgrammeSpecialty.sql")
  @Sql(scripts = "/scripts/deletePlacementProgrammeSpecialty.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void findPlacementsByPostIdsShouldFindPlacementsLinkedToSpecialtyAndProgramme() {
    Long placementId1 = 3L, placementId2 = 30L;
    Long programmeId = 5L;
    Long specialtyId = 1L;
    Long traineeId1 = 4L, traineeId2 = 40L;
    Long postId1 = 2L;
    String traineeForename1 = "John", traineeForename2 = "Joanne";
    Set<Long> postIds = new HashSet<>();
    postIds.add(postId1);

    Set<Placement> results = testObj.findPlacementsByPostIds(postIds);

    Assert.assertNotNull(results);
    Assert.assertEquals(2, results.size());

    for (Placement placement : results) {
      Assert.assertTrue(placementId1.equals(placement.getId()) || placementId2.equals(placement.getId()));
      Assert.assertEquals(specialtyId, placement.getSpecialties().iterator().next().getSpecialty().getId());
      Assert.assertEquals(programmeId, placement.getPost().getProgrammes().iterator().next().getId());

      Assert.assertTrue(traineeId1.equals(placement.getTrainee().getId()) || traineeId2.equals(placement.getTrainee().getId()));
      Assert.assertTrue(traineeForename1.equals(placement.getTrainee().getContactDetails().getForenames()) || traineeForename2.equals(placement.getTrainee().getContactDetails().getForenames()));
    }

  }
}
