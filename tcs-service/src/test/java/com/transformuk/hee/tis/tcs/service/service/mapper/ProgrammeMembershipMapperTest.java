package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.time.LocalDate;
import java.util.List;
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
    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership();
    CurriculumMembership cm1 = new CurriculumMembership(), cm2 = new CurriculumMembership(),
        cm3 = new CurriculumMembership();
    ProgrammeMembershipDTO pmDTO = new ProgrammeMembershipDTO();
    pmDTO.setProgrammeMembershipId(12345L);
    pmDTO.setId(1L);

    pm1.setId(12345L);
    pm2.setId(99876L);

    cm1.setId(1L);
    cm2.setId(2L);
    cm3.setId(3L);
    cm1.setProgrammeMembership(pm1);
    cm2.setProgrammeMembership(pm2);
    cm3.setProgrammeMembership(pm2);
    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2, cm3));

    LocalDate programmeStartDate1 = LocalDate.now(), programmeStartDate2 = LocalDate.of(1999, 1, 1);
    pm1.setProgrammeStartDate(programmeStartDate1);
    pm2.setProgrammeStartDate(programmeStartDate1);

    LocalDate programmeEndDate1 = LocalDate.now(), programmeEndDate2 = LocalDate.of(2000, 1, 1);
    pm1.setProgrammeEndDate(programmeEndDate1);
    pm2.setProgrammeEndDate(programmeEndDate1);

    Person person1 = new Person();
    person1.setId(1L);

    pm1.setPerson(person1);
    pm2.setPerson(person1);

    when(curriculumMembershipMapperMock.toDto(cm1)).thenReturn(pmDTO);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(pm1, pm2));

    Assert.assertEquals(3, result.size()); //listed by curriculum membership
    ProgrammeMembershipDTO pmDTOreturned = result.get(0);
    Assert.assertNotNull(pmDTOreturned);
    assertThat(pmDTO.getId()).isEqualTo(pmDTOreturned.getId()); //check that cm1 is the first entity converted to a DTO
    assertThat(pmDTO.getProgrammeMembershipId()).isEqualTo(pmDTOreturned.getProgrammeMembershipId());
  }
}
