package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RotationDtoMapperTest {

  private static final Long ROTATION_ID_1 = 1L;
  private static final Long ROTATION_ID_2 = 2L;
  private static final Long PROGRAMME_ID_1 = 1L;
  private static final Long PROGRAMME_ID_2 = 2L;
  private static final String PROGRAMME_NAME_1 = "programmeName1";
  private static final String PROGRAMME_NAME_2 = "programmeName2";

  private static final String PROGRAMME_NUMBER_1 = "programmeNumber1";
  private static final String PROGRAMME_NUMBER_2 = "programmeNumber2";
  private static final String ROTATION_NAME_1 = "rotationName1";
  private static final String ROTATION_NAME_2 = "rotationName2";
  private static final Status ROTATION_STATUS_1 = Status.CURRENT;
  private static final Status ROTATION_STATUS_2 = Status.INACTIVE;

  private RotationDtoMapper mapper;
  private RotationDTO source;
  private RotationDTO target;

  @BeforeEach
  void setUp() {
    mapper = new RotationDtoMapperImpl();

    source = new RotationDTO();
    source.setId(ROTATION_ID_1);
    source.setName(ROTATION_NAME_1);
    source.setStatus(ROTATION_STATUS_1);
    source.setProgrammeName(PROGRAMME_NAME_1);
    source.setProgrammeId(PROGRAMME_ID_1);
    source.setProgrammeNumber(PROGRAMME_NUMBER_1);

    target = new RotationDTO();
    target.setId(ROTATION_ID_2);
    target.setName(ROTATION_NAME_2);
    target.setStatus(ROTATION_STATUS_2);
    target.setProgrammeName(PROGRAMME_NAME_2);
    target.setProgrammeId(PROGRAMME_ID_2);
    target.setProgrammeNumber(PROGRAMME_NUMBER_2);
  }

  @Test
  void shouldCopyValuesIfNotNull() {
    mapper.copyIfNotNull(source, target);

    Assert.assertEquals(ROTATION_ID_1, target.getId());
    Assert.assertEquals(ROTATION_NAME_1, target.getName());
    Assert.assertEquals(ROTATION_STATUS_1, target.getStatus());
    Assert.assertEquals(PROGRAMME_ID_1, target.getProgrammeId());
    Assert.assertEquals(PROGRAMME_NAME_1, target.getProgrammeName());
    Assert.assertEquals(PROGRAMME_NUMBER_1, target.getProgrammeNumber());
  }

  @Test
  void shouldNotCopyNullValues() {
    RotationDTO sourceNullValues = new RotationDTO();

    mapper.copyIfNotNull(sourceNullValues, target);

    Assert.assertEquals(ROTATION_ID_2, target.getId());
    Assert.assertEquals(ROTATION_NAME_2, target.getName());
    Assert.assertEquals(ROTATION_STATUS_2, target.getStatus());
    Assert.assertEquals(PROGRAMME_ID_2, target.getProgrammeId());
    Assert.assertEquals(PROGRAMME_NAME_2, target.getProgrammeName());
    Assert.assertEquals(PROGRAMME_NUMBER_2, target.getProgrammeNumber());
  }
}