package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlacementRepositoryTest {

  @Autowired
  private PlacementRepository testObj;

  @Transactional
  @Test
  @Sql(scripts = "/scripts/placementProgrammeSpecialty.sql")
  @Sql(scripts = "/scripts/deletePlacementProgrammeSpecialty.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void findPlacementsByPostIdsShouldFindPlacementsLinkedToSpecialtyAndProgramme() {
    Long placementId1 = 3L, placementId2 = 30L;
    Long programmeId = 5L;
    Long specialtyId = 1L;
    Long traineeId1 = 4L, traineeId2 = 40L;
    Long postId1 = 2L;
    String traineeForename1 = "John", traineeForename2 = "Joanne";
    Set<Long> postIds = new HashSet<>();
    postIds.add(postId1);

    Set<Placement> results = testObj.findPlacementsByPostIds(postIds);

    assertNotNull(results);
    assertEquals(2, results.size());

    for (Placement placement : results) {
      assertTrue(
          placementId1.equals(placement.getId()) || placementId2.equals(placement.getId()));
      assertEquals(specialtyId,
          placement.getSpecialties().iterator().next().getSpecialty().getId());
      assertEquals(programmeId, placement.getPost().getProgrammes().iterator().next().getId());

      assertTrue(traineeId1.equals(placement.getTrainee().getId()) || traineeId2
          .equals(placement.getTrainee().getId()));
      assertTrue(
          traineeForename1.equals(placement.getTrainee().getContactDetails().getForenames())
              || traineeForename2
              .equals(placement.getTrainee().getContactDetails().getForenames()));
    }

  }

  @Transactional
  @Test
  @Sql(scripts = "/scripts/person.sql")
  @Sql(scripts = "/scripts/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void findAllCurrentPlacementsForTraineeShouldUseCurrentDateForBothBounds() {
    Long traineeId = 1L;
    LocalDate currentDate = LocalDate.of(2022, Month.SEPTEMBER, 8);

    List<Placement> results = testObj.findAllCurrentPlacementsForTrainee(traineeId, currentDate);

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(Long.valueOf(3L), results.get(0).getId());
  }

  @Transactional
  @Test
  @Sql(scripts = "/scripts/person.sql")
  @Sql(scripts = "/scripts/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void findAllCurrentPlacementsForTraineeShouldNotIncludePlacementsWithNullEndDate() {
    Long traineeId = 1L;
    LocalDate currentDate = LocalDate.of(2022, Month.SEPTEMBER, 8);

    List<Placement> results = testObj.findAllCurrentPlacementsForTrainee(traineeId, currentDate);

    assertNotNull(results);
    assertEquals(1, results.size());
    Set<Long> placementIds = results.stream().map(Placement::getId).collect(Collectors.toSet());
    assertTrue(placementIds.contains(3L));
  }
}
