package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfigNonES;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import java.util.List;
import java.util.Set;
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
public class PlacementEsrEventRepositoryTest {

  @Autowired
  private PlacementEsrEventRepository placementEsrEventRepository;

  @Sql(scripts = "/scripts/insertPlacementEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePlacementEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPlacementEsrEventByPlacementIdInShouldReturnEventsForAllProvidedPlacements(){
    long firstPlacement = 1111111L;
    long secondPlacement = 2222222L;
    List<Long> placementIds = Lists.newArrayList(firstPlacement, secondPlacement);

    Set<PlacementEsrEvent> result = placementEsrEventRepository
        .findPlacementEsrEventByPlacementIdIn(placementIds);

    Assert.assertNotNull(result);
    Assert.assertEquals(3, result.size());

    for (PlacementEsrEvent placementEsrEvent : result) {
      Assert.assertTrue(placementEsrEvent.getPlacement().getId().equals(firstPlacement) ||
          placementEsrEvent.getPlacement().getId().equals(secondPlacement));
    }
  }

  @Sql(scripts = "/scripts/insertPlacementEsrEvent.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePlacementEsrEvent.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void findPlacementEsrEventByPlacementIdInShouldReturnEmptyListWhenNoMatchingPlacementIdFound(){
    long nonExistingPlacementId = 9999L;
    List<Long> placementIds = Lists.newArrayList(nonExistingPlacementId);

    Set<PlacementEsrEvent> result = placementEsrEventRepository
        .findPlacementEsrEventByPlacementIdIn(placementIds);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

}
