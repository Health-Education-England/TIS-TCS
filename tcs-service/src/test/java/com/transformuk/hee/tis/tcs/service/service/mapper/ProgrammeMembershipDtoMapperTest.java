package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgrammeMembershipDtoMapperTest {

  private static final UUID UUID_1 = UUID.randomUUID();
  private static final UUID UUID_2 = UUID.randomUUID();

  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE_1 =
      ProgrammeMembershipType.LAT;
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE_2 =
      ProgrammeMembershipType.ACADEMIC;
  private static final LocalDate PROGRAMME_START_DATE_1 =
      LocalDate.of(2023, Month.AUGUST, 22);
  private static final LocalDate PROGRAMME_START_DATE_2 =
      LocalDate.of(2023, Month.AUGUST, 23);

  private static final LocalDate PROGRAMME_END_DATE_1 =
      LocalDate.of(2024, Month.AUGUST, 22);
  private static final LocalDate PROGRAMME_END_DATE_2 =
      LocalDate.of(2024, Month.AUGUST, 23);
  private static final String LEAVING_REASON_1 = "leavingReason1";
  private static final String LEAVING_REASON_2 = "leavingReason2";

  private static final String TRAINING_PATHWAY_1 = "trainingPathway1";
  private static final String TRAINING_PATHWAY_2 = "trainingPathway2";

  private static final String ROTATION_NAME_1 = "rotation1";
  private static final String ROTATION_NAME_2 = "rotation2";
  private static final Long ROTATION_ID_2 = 1L;
  private static final String ERR_MSG = "error";

  private ProgrammeMembershipDtoMapper mapper;
  private ProgrammeMembershipDTO source;
  private ProgrammeMembershipDTO target;

  @BeforeEach
  void setUp() {
    mapper = new ProgrammeMembershipDtoMapperImpl();

    source = new ProgrammeMembershipDTO();
    source.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE_1);
    source.setUuid(UUID_1);
    source.setLeavingReason(LEAVING_REASON_1);
    source.setTrainingPathway(TRAINING_PATHWAY_1);
    source.setProgrammeStartDate(PROGRAMME_START_DATE_1);
    source.setProgrammeEndDate(PROGRAMME_END_DATE_1);
    RotationDTO sourceRotationDto = new RotationDTO();
    sourceRotationDto.setName(ROTATION_NAME_1);
    source.setRotation(sourceRotationDto);
    source.addMessage(ERR_MSG);

    target = new ProgrammeMembershipDTO();
    target.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE_2);
    target.setUuid(UUID_2);
    target.setLeavingReason(LEAVING_REASON_2);
    target.setTrainingPathway(TRAINING_PATHWAY_2);
    target.setProgrammeStartDate(PROGRAMME_START_DATE_2);
    target.setProgrammeEndDate(PROGRAMME_END_DATE_2);
    RotationDTO targetRotationDto = new RotationDTO();
    targetRotationDto.setId(ROTATION_ID_2);
    targetRotationDto.setName(ROTATION_NAME_2);
    target.setRotation(targetRotationDto);
  }

  @Test
  void shouldCopyValuesIfNotNull() {
    mapper.copyIfNotNull(source, target);

    Assert.assertEquals(PROGRAMME_MEMBERSHIP_TYPE_1, target.getProgrammeMembershipType());
    Assert.assertEquals(UUID_1, target.getUuid());
    Assert.assertEquals(LEAVING_REASON_1, target.getLeavingReason());
    Assert.assertEquals(TRAINING_PATHWAY_1, target.getTrainingPathway());
    Assert.assertEquals(PROGRAMME_START_DATE_1, target.getProgrammeStartDate());
    Assert.assertEquals(PROGRAMME_END_DATE_1, target.getProgrammeEndDate());
    Assert.assertEquals(ROTATION_NAME_1, target.getRotation().getName());
    Assert.assertNull(target.getRotation().getId());
    Assert.assertEquals(1, target.getMessageList().size());
    Assert.assertEquals(ERR_MSG, target.getMessageList().get(0));
  }

  @Test
  void shouldNotCopyNullValues() {
    ProgrammeMembershipDTO sourceNullValues = new ProgrammeMembershipDTO();
    mapper.copyIfNotNull(sourceNullValues, target);

    Assert.assertEquals(PROGRAMME_MEMBERSHIP_TYPE_2, target.getProgrammeMembershipType());
    Assert.assertEquals(UUID_2, target.getUuid());
    Assert.assertEquals(LEAVING_REASON_2, target.getLeavingReason());
    Assert.assertEquals(TRAINING_PATHWAY_2, target.getTrainingPathway());
    Assert.assertEquals(PROGRAMME_START_DATE_2, target.getProgrammeStartDate());
    Assert.assertEquals(PROGRAMME_END_DATE_2, target.getProgrammeEndDate());
    Assert.assertEquals(ROTATION_NAME_2, target.getRotation().getName());
    Assert.assertEquals(0, target.getMessageList().size());
  }
}
