package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
<<<<<<< HEAD
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
=======
>>>>>>> feat/cojIntoPmDtoV3squashed
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
<<<<<<< HEAD
import java.time.Instant;
import java.util.List;
=======
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
>>>>>>> feat/cojIntoPmDtoV3squashed
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurriculumMembershipMapperTest {

  @Mock
  ConditionsOfJoiningMapper conditionsOfJoiningMapperMock;

  @InjectMocks
  private CurriculumMembershipMapper testObj;

  @Test
  public void entityToDtoShouldReturnListOfAllElementsAsDto() {
    UUID pmID = UUID.randomUUID();
    UUID pm2ID = UUID.randomUUID();
    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership();
    CurriculumMembership cm1 = new CurriculumMembership(), cm2 = new CurriculumMembership();
    ConditionsOfJoining conditionsOfJoining = new ConditionsOfJoining();

    pm1.setUuid(pmID);
    pm2.setUuid(pm2ID);

    cm1.setId(1L);
    cm2.setId(2L);
    cm1.setProgrammeMembership(pm1);
    cm2.setProgrammeMembership(pm2);
    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2));

    conditionsOfJoining.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoining.setProgrammeMembershipUuid(pmID);
    conditionsOfJoining.setSignedAt(Instant.now());
    pm1.setConditionsOfJoining(conditionsOfJoining);
    ConditionsOfJoiningMapper conditionsOfJoiningMapper = new ConditionsOfJoiningMapperImpl();
    ConditionsOfJoiningDto conditionsOfJoiningDto
        = conditionsOfJoiningMapper.toDto(conditionsOfJoining);
    pm1.setConditionsOfJoining(conditionsOfJoining);
    when(conditionsOfJoiningMapperMock.toDto(conditionsOfJoining))
        .thenReturn(conditionsOfJoiningDto);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(cm1, cm2));

    Assert.assertEquals(2, result.size()); //listed by curriculum membership
    ProgrammeMembershipDTO actualPmDto = result.get(0);
    Assert.assertNotNull(actualPmDto);
    assertThat(cm1.getId()).isEqualTo(actualPmDto.getId()); //check that cm ID is used as pm ID
    assertThat(pmID).isEqualTo(actualPmDto.getUuid());

    //check Condition of joining is attached to first PmDto and the other is null
    assertThat(actualPmDto.getConditionsOfJoining()).isEqualTo(conditionsOfJoiningDto);
    assertThat(result.get(1).getConditionsOfJoining()).isNull();
  }
}
