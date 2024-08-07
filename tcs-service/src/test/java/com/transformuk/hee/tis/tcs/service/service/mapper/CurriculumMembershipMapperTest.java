package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import java.util.List;
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

  @Mock
  ConditionsOfJoiningRepository conditionsOfJoiningRepositoryMock;

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
    pm1.setConditionsOfJoining(conditionsOfJoining);
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2));

    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();

    when(conditionsOfJoiningMapperMock.toDto(conditionsOfJoining))
        .thenReturn(conditionsOfJoiningDto);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(cm1, cm2));

    Assert.assertEquals(2, result.size()); //listed by curriculum membership
    ProgrammeMembershipDTO pmDTOreturned = result.get(0);
    Assert.assertNotNull(pmDTOreturned);
    assertThat(pmDTOreturned.getId()).isEqualTo(cm1.getId()); //check that cm ID is used as pm ID
    assertThat(pmDTOreturned.getUuid()).isEqualTo(pmID);

    //check Condition of joining is attached to first PmDto and the other is null
    assertThat(pmDTOreturned.getConditionsOfJoining()).isEqualTo(conditionsOfJoiningDto);
    assertThat(result.get(1).getConditionsOfJoining()).isNull();
  }
}
