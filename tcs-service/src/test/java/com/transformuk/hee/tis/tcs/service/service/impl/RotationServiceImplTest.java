package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationMapperImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RotationServiceImplTest {

  private static final String ROTATION_NAME = "rotationName";
  private static final Long PROGRAMME_ID = 1L;

  @InjectMocks
  private RotationServiceImpl testObj;

  @Mock
  private RotationRepository rotationRepository;
  @Mock
  private ProgrammeRepository programmeRepository;

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(testObj, "rotationMapper",
        new RotationMapperImpl());
  }

  @Test
  public void ShouldGetCurrentRotationsByNameAndProgrammeId() {
    Rotation rotation = new Rotation();
    rotation.setName(ROTATION_NAME);
    rotation.setProgrammeId(PROGRAMME_ID);

    when(rotationRepository.findCurrentByNameAndProgrammeId(ROTATION_NAME, PROGRAMME_ID))
        .thenReturn(Lists.newArrayList(rotation));

    List<RotationDTO> rotationDtos = testObj.getCurrentRotationsByNameAndProgrammeId(
        ROTATION_NAME, PROGRAMME_ID);

    Assert.assertEquals(1, rotationDtos.size());
    Assert.assertEquals(ROTATION_NAME, rotationDtos.get(0).getName());
    Assert.assertEquals(PROGRAMME_ID, rotationDtos.get(0).getProgrammeId());
  }
}