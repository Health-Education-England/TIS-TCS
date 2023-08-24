package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class RotationRepositoryTest {

  private static final String ROTATION_NAME = "rotationName";
  private static final Long PROGRAMME_ID = 1L;
  @Autowired
  RotationRepository rotationRepository;

  Rotation rotation1;
  Rotation rotation2;

  @Before
  public void setUp() {
    rotation1 = new Rotation();
    rotation1.setName(ROTATION_NAME);
    rotation1.programmeId(PROGRAMME_ID);
    rotation1.setStatus(Status.CURRENT);
    rotation1 = rotationRepository.saveAndFlush(rotation1);

    rotation2 = new Rotation();
    rotation2.setName(ROTATION_NAME);
    rotation2.setProgrammeId(PROGRAMME_ID);
    rotation2.setStatus(Status.INACTIVE);
    rotation2 = rotationRepository.saveAndFlush(rotation2);
  }

  @Test
  public void shouldReturnCurrentRotationByNameAndProgrammeId() {
    List<Rotation> allRotations = rotationRepository.findAll();

    List<Rotation> rotations = rotationRepository.findCurrentByNameAndProgrammeId(ROTATION_NAME,
        PROGRAMME_ID);
    Assert.assertEquals(2, allRotations.size());
    Assert.assertEquals(1, rotations.size());
    Assert.assertEquals(Status.CURRENT, rotations.get(0).getStatus());
  }
}