package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipMapperTest {

  @Mock
  CurriculumMembershipMapper curriculumMembershipMapperMock;

  @InjectMocks
  private ProgrammeMembershipMapper testObj;

  @Test
  public void entityToDtoShouldReturnListOfAllElementsInAsDto() {
    UUID pmID = UUID.randomUUID();
    UUID pm2ID = UUID.randomUUID();
    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership();
    CurriculumMembership cm1 = new CurriculumMembership(), cm2 = new CurriculumMembership(),
        cm3 = new CurriculumMembership();

    pm1.setId(pmID);
    pm2.setId(pm2ID);

    cm1.setId(1L);
    cm2.setId(2L);
    cm3.setId(3L);
    cm1.setProgrammeMembership(pm1);
    cm2.setProgrammeMembership(pm2);
    cm3.setProgrammeMembership(pm2);
    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2, cm3));

    CurriculumMembershipDTO cmDTO1 = new CurriculumMembershipDTO();
    cmDTO1.setId(1L);
    CurriculumMembershipDTO cmDTO2 = new CurriculumMembershipDTO();
    cmDTO2.setId(2L);
    CurriculumMembershipDTO cmDTO3 = new CurriculumMembershipDTO();
    cmDTO3.setId(3L);

    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm1)).thenReturn(cmDTO1);
    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm2)).thenReturn(cmDTO2);
    when(curriculumMembershipMapperMock.curriculumMembershipToCurriculumMembershipDto(cm3)).thenReturn(cmDTO3);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(pm1, pm2));

    Assert.assertEquals(3, result.size()); //listed by curriculum membership
    ProgrammeMembershipDTO pmDTOreturned = result.get(0);
    Assert.assertNotNull(pmDTOreturned);
    assertThat(cm1.getId()).isEqualTo(pmDTOreturned.getId()); //check that cm ID is used as pm ID
    assertThat(pmID).isEqualTo(pmDTOreturned.getProgrammeMembershipId());
  }
}
