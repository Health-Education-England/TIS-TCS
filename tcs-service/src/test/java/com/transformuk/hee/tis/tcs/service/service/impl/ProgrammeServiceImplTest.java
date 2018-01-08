package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeServiceImplTest {

  public static final long PERSON_ID = 123L;
  @InjectMocks
  private ProgrammeServiceImpl testObj;

  @Mock
  private ProgrammeRepository programmeRepositoryMock;

  @Mock
  private ProgrammeMapper programmeMapperMock;


  @Test(expected = NullPointerException.class)
  public void findTraineeProgrammesShouldNPEWhenNullIsProvided() {
    try {
      testObj.findTraineeProgrammes(null);
    } catch (Exception e) {
      verify(programmeRepositoryMock, never()).findByProgrammeMembershipPersonId(any());
      verify(programmeMapperMock, never()).programmesToProgrammeDTOs(anyList());
      throw e;
    }
  }

  @Test()
  public void findTraineeProgrammesShouldReturnAnEmptyListWhenNoProgrammesFound() {
    List<Programme> foundProgrammes = Lists.newArrayList();
    List<ProgrammeDTO> convertedProgrammes = Lists.newArrayList();
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID)).thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes)).thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
    Assert.assertEquals(convertedProgrammes, result);
  }

  @Test()
  public void findTraineeProgrammesShouldReturnDtosOfProgrammes() {
    Programme programme1 = new Programme();
    programme1.setId(1L);
    programme1.setProgrammeName("AAAAA");

    Programme programme2 = new Programme();
    programme2.setId(2L);
    programme2.setProgrammeName("BBBBB");

    ProgrammeDTO programmeDTO1 = new ProgrammeDTO();
    programmeDTO1.setId(1L);
    programmeDTO1.setProgrammeName("AAAAA");

    ProgrammeDTO programmeDTO2 = new ProgrammeDTO();
    programmeDTO2.setId(2L);
    programmeDTO2.setProgrammeName("BBBBB");

    List<Programme> foundProgrammes = Lists.newArrayList(programme1, programme2);
    List<ProgrammeDTO> convertedProgrammes = Lists.newArrayList(programmeDTO1, programmeDTO2);
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID)).thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes)).thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(convertedProgrammes, result);
  }

}