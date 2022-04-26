package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipMapperTest {

  @InjectMocks
  private ProgrammeMembershipMapper testObj;

  @Test
  public void entityToDtoShouldReturnListOfAllElementsInAsDto() {
    //programme membership 1 and 2 are to have same programme data but different curricula
    //the issue was that the equals method use programme fields to check for equality and the the mapper method used a map collection using
    //the dto as the key (therefore not converting all of the entities when data is duplicated)
    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership(), pm3 = new ProgrammeMembership();

    pm1.setId(12345L);
    pm2.setId(99876L);
    pm3.setId(45667L);

    LocalDate programmeStartDate1 = LocalDate.now(), programmeStartDate2 = LocalDate.of(1999, 1, 1);
    pm1.setProgrammeStartDate(programmeStartDate1);
    pm2.setProgrammeStartDate(programmeStartDate1);
    pm3.setProgrammeStartDate(programmeStartDate2);

    LocalDate programmeEndDate1 = LocalDate.now(), programmeEndDate2 = LocalDate.of(2000, 1, 1);
    pm1.setProgrammeEndDate(programmeEndDate1);
    pm2.setProgrammeEndDate(programmeEndDate1);
    pm3.setProgrammeEndDate(programmeEndDate2);

    Person person1 = new Person();
    person1.setId(1L);

    pm1.setPerson(person1);
    pm2.setPerson(person1);
    pm3.setPerson(person1);

    List<ProgrammeMembershipDTO> result = testObj.allEntityToDto(Lists.newArrayList(pm1, pm2, pm3));

    Assert.assertEquals(3, result.size());
  }
}
