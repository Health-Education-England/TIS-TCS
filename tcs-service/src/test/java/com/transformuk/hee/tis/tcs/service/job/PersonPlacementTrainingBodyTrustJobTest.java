package com.transformuk.hee.tis.tcs.service.job;

import com.transformuk.hee.tis.tcs.service.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PersonPlacementTrainingBodyTrustJobTest {

  @Autowired
  private PersonPlacementTrainingBodyTrustJob testObj;

  @Test
  public void runShouldNotHaveConnectionPoolIssuesAfterRunningMultipleTimes() {
    for (int i = 0; i < 30; i++) {
      testObj.run();
    }
  }
}