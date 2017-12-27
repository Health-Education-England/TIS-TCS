package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipServiceImplTest {

  private static final long TRAINEE_ID = 1L;
  private static final long PROGRAMME_ID = 2L;

  @InjectMocks
  private ProgrammeMembershipServiceImpl testObj;

  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;

  @Mock
  private ProgrammeMembershipMapper programmeMembershipMapperMock;

  @Captor
  private ArgumentCaptor<Example<ProgrammeMembership>> exampleArgumentCaptor;

  private ProgrammeMembership programmeMembership1 = new ProgrammeMembership(), programmeMembership2 = new ProgrammeMembership();
  private ProgrammeMembershipDTO programmeMembershipDTO1 = new ProgrammeMembershipDTO(), programmeMembershipDTO2 = new ProgrammeMembershipDTO();


  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(null, PROGRAMME_ID);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never()).findAll(any(Example.class));
      verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenProgrammeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, null);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never()).findAll(any(Example.class));
      verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnEmptyListWhenNoResultsFound() {
    List<ProgrammeMembership> emptyProgrammeMembershipList = Lists.emptyList();
    List<ProgrammeMembershipDTO> emptyProgrammeMembershipDTOList = Lists.emptyList();
    when(programmeMembershipRepositoryMock.findAll(exampleArgumentCaptor.capture())).thenReturn(emptyProgrammeMembershipList);
    when(programmeMembershipMapperMock.programmeMembershipsToProgrammeMembershipDTOs(emptyProgrammeMembershipList)).thenReturn(emptyProgrammeMembershipDTOList);

    List<ProgrammeMembershipDTO> result = testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0L, result.size());
    Example<ProgrammeMembership> capturedExample = exampleArgumentCaptor.getValue();

    ProgrammeMembership programmeMembershipExample = capturedExample.getProbe();

    Assert.assertEquals(new Long(TRAINEE_ID), programmeMembershipExample.getPerson().getId());
    Assert.assertEquals(new Long(PROGRAMME_ID), programmeMembershipExample.getProgrammeId());

    verify(programmeMembershipRepositoryMock).findAll(capturedExample);
    verify(programmeMembershipMapperMock).programmeMembershipsToProgrammeMembershipDTOs(emptyProgrammeMembershipList);
  }


  @Test()
  public void findProgrammeMembershipsForTraineeAndProgramme() {
    programmeMembership1.setProgrammeId(PROGRAMME_ID);
    programmeMembership1.setTrainingNumberId(TRAINEE_ID);
    programmeMembership1.setCurriculumId(5L);

    programmeMembership2.setProgrammeId(PROGRAMME_ID);
    programmeMembership2.setTrainingNumberId(TRAINEE_ID);
    programmeMembership2.setCurriculumId(6L);

    programmeMembershipDTO1.setProgrammeId(PROGRAMME_ID);
    programmeMembershipDTO1.setTrainingNumberId(TRAINEE_ID);
    programmeMembershipDTO1.setCurriculumId(5L);

    programmeMembershipDTO2.setProgrammeId(PROGRAMME_ID);
    programmeMembershipDTO2.setTrainingNumberId(TRAINEE_ID);
    programmeMembershipDTO2.setCurriculumId(6L);

    List<ProgrammeMembership> programmeMemberships = Lists.newArrayList(programmeMembership1, programmeMembership2);
    List<ProgrammeMembershipDTO> programmeMembershipDTOList = Lists.newArrayList(programmeMembershipDTO1, programmeMembershipDTO2);
    when(programmeMembershipRepositoryMock.findAll(exampleArgumentCaptor.capture())).thenReturn(programmeMemberships);
    when(programmeMembershipMapperMock.programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships)).thenReturn(programmeMembershipDTOList);

    List<ProgrammeMembershipDTO> result = testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(2L, result.size());
    Example<ProgrammeMembership> capturedExample = exampleArgumentCaptor.getValue();

    ProgrammeMembership programmeMembershipExample = capturedExample.getProbe();

    Assert.assertEquals(new Long(TRAINEE_ID), programmeMembershipExample.getPerson().getId());
    Assert.assertEquals(new Long(PROGRAMME_ID), programmeMembershipExample.getProgrammeId());

    verify(programmeMembershipRepositoryMock).findAll(capturedExample);
    verify(programmeMembershipMapperMock).programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships);
  }

}