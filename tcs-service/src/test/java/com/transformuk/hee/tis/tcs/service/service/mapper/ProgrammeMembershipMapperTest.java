package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.util.List;
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipMapperTest {

  @Mock
  CurriculumMembershipMapper curriculumMembershipMapperMock;

  @Mock
  ConditionsOfJoiningMapper conditionsOfJoiningMapperMock;

  @InjectMocks
  private ProgrammeMembershipMapper testObj;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(testObj, "trainingNumberMapper",
        new TrainingNumberMapperImpl());
  }

  @Test
  public void entityToDtoShouldReturnListOfAllElementsInAsDto() {
    UUID pmID = UUID.randomUUID();
    UUID pm2ID = UUID.randomUUID();
    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership();
    CurriculumMembership cm1 = new CurriculumMembership(), cm2 = new CurriculumMembership(),
        cm3 = new CurriculumMembership();
    ConditionsOfJoining conditionsOfJoining = new ConditionsOfJoining();

    pm1.setUuid(pmID);
    pm2.setUuid(pm2ID);

    cm1.setId(1L);
    cm2.setId(2L);
    cm3.setId(3L);
    cm1.setProgrammeMembership(pm1);
    cm2.setProgrammeMembership(pm2);
    cm3.setProgrammeMembership(pm2);
    pm1.setConditionsOfJoining(conditionsOfJoining);
    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2, cm3));

    CurriculumMembershipDTO cmDTO1 = new CurriculumMembershipDTO();
    cmDTO1.setId(1L);
    CurriculumMembershipDTO cmDTO2 = new CurriculumMembershipDTO();
    cmDTO2.setId(2L);
    CurriculumMembershipDTO cmDTO3 = new CurriculumMembershipDTO();
    cmDTO3.setId(3L);

    ConditionsOfJoiningDto conditionsOfJoiningDto = new ConditionsOfJoiningDto();

    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm1))
        .thenReturn(cmDTO1);
    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm2))
        .thenReturn(cmDTO2);
    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm3))
        .thenReturn(cmDTO3);
    when(conditionsOfJoiningMapperMock.toDto(conditionsOfJoining))
        .thenReturn(conditionsOfJoiningDto);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(pm1, pm2));

    Assert.assertEquals(3, result.size()); //listed by curriculum membership
    ProgrammeMembershipDTO pmDTOreturned = result.get(0);
    Assert.assertNotNull(pmDTOreturned);
    assertThat(pmDTOreturned.getId()).isEqualTo(cm1.getId()); //check that cm ID is used as pm ID
    assertThat(pmDTOreturned.getUuid()).isEqualTo(pmID);

    //check Condition of joining is attached to first PmDto and others are null
    assertThat(pmDTOreturned.getConditionsOfJoining()).isEqualTo(conditionsOfJoiningDto);
    assertThat(result.get(1).getConditionsOfJoining()).isNull();
    assertThat(result.get(2).getConditionsOfJoining()).isNull();
  }
}
