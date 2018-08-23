package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipServiceImplTest {

  private static final long TRAINEE_ID = 1L;
  private static final String TRAINEE_NUMBER = "XXX/XXX/XXX";

  private static final long PROGRAMME_ID = 2L;
  private static final String PROGRAMME_NUMBER = "XXXXX";
  private static final Long PROGRAMME_MEMBERSHIP_ID_1 = 7777L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_2 = 8888L;
  private static final String PROGRAMME_NUMBER1 = "Programme number";
  private static final String PROGRAMME_NAME = "Programme Name";

  @InjectMocks
  private ProgrammeMembershipServiceImpl testObj;

  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private ProgrammeMembershipMapper programmeMembershipMapperMock;
  @Mock
  private CurriculumRepository curriculumRepositoryMock;
  @Mock
  private CurriculumMapper curriculumMapperMock;
  @Mock
  private ProgrammeRepository programmeRepositoryMock;

  private ProgrammeMembership programmeMembership1 = new ProgrammeMembership(), programmeMembership2 = new ProgrammeMembership();
  private ProgrammeMembershipDTO programmeMembershipDTO1 = new ProgrammeMembershipDTO(), programmeMembershipDTO2 = new ProgrammeMembershipDTO();
  private Curriculum curriculum1 = new Curriculum(), curriculum2 = new Curriculum();
  private CurriculumDTO curriculumDTO1 = new CurriculumDTO(), curriculumDTO2 = new CurriculumDTO();
  private TrainingNumber trainingNumber = new TrainingNumber();
  private TrainingNumberDTO trainingNumberDTO = new TrainingNumberDTO();
  private CurriculumMembershipDTO curriculumMembershipDTO1 = new CurriculumMembershipDTO(), curriculumMembershipDTO2 = new CurriculumMembershipDTO();;
  private Programme programme = new Programme();

  @Before
  public void setup() {

    trainingNumber.setId(TRAINEE_ID);
    trainingNumber.setTrainingNumber(TRAINEE_NUMBER);

    programme.setId(PROGRAMME_ID);
    programme.setProgrammeNumber(PROGRAMME_NUMBER1);
    programme.setProgrammeName(PROGRAMME_NAME);

    programmeMembership1.setId(PROGRAMME_MEMBERSHIP_ID_1);
    programmeMembership1.setProgramme(programme);
    programmeMembership1.setTrainingNumber(trainingNumber);
    programmeMembership1.setCurriculumId(5L);

    programmeMembership2.setId(PROGRAMME_MEMBERSHIP_ID_2);
    programmeMembership2.setProgramme(programme);
    programmeMembership2.setTrainingNumber(trainingNumber);
    programmeMembership2.setCurriculumId(6L);

    trainingNumberDTO.setId(TRAINEE_ID);
    trainingNumberDTO.setTrainingNumber(TRAINEE_NUMBER);
    programmeMembershipDTO1.setProgrammeId(PROGRAMME_ID);
    programmeMembershipDTO1.setTrainingNumber(trainingNumberDTO);
    programmeMembershipDTO1.setCurriculumMemberships(Lists.newArrayList());

    programmeMembershipDTO2.setProgrammeId(PROGRAMME_ID);
    programmeMembershipDTO2.setTrainingNumber(trainingNumberDTO);
    programmeMembershipDTO2.setCurriculumMemberships(Lists.newArrayList());

    curriculumMembershipDTO1.setCurriculumId(5L);
    curriculumMembershipDTO2.setCurriculumId(6L);

    curriculum1.setId(5L);
    curriculum1.setName("XXX");
    curriculum2.setId(6L);
    curriculum2.setName("YYY");

    curriculumDTO1.setId(5L);
    curriculumDTO1.setName("XXX");
    curriculumDTO2.setId(6L);
    curriculumDTO2.setName("YYY");

  }

  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(null, PROGRAMME_ID);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never()).findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
      verify(curriculumRepositoryMock, never()).findAll(anyList());
      verify(curriculumMapperMock, never()).curriculumToCurriculumDTO(any(Curriculum.class));
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenProgrammeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, null);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never()).findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
      verify(curriculumRepositoryMock, never()).findAll(anyList());
      verify(curriculumMapperMock, never()).curriculumToCurriculumDTO(any(Curriculum.class));
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnEmptyListWhenNoResultsFound() {
    List<ProgrammeMembership> emptyProgrammeMembershipList = Lists.emptyList();
    List<ProgrammeMembershipDTO> emptyProgrammeMembershipDTOList = Lists.emptyList();
    when(programmeMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID)).thenReturn(emptyProgrammeMembershipList);
    when(programmeMembershipMapperMock.programmeMembershipsToProgrammeMembershipDTOs(emptyProgrammeMembershipList)).thenReturn(emptyProgrammeMembershipDTOList);

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0L, result.size());

    verify(curriculumRepositoryMock, never()).findAll(anyList());
    verify(curriculumMapperMock, never()).curriculumToCurriculumDTO(any(Curriculum.class));

    verify(programmeMembershipRepositoryMock).findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID);
    verify(programmeMembershipMapperMock).programmeMembershipsToProgrammeMembershipDTOs(emptyProgrammeMembershipList);
  }


  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnPopulatedDTO() {

    programmeMembershipDTO1.getCurriculumMemberships().add(curriculumMembershipDTO1);
    programmeMembershipDTO2.getCurriculumMemberships().add(curriculumMembershipDTO2);

    List<ProgrammeMembership> programmeMemberships = Lists.newArrayList(programmeMembership1, programmeMembership2);
    List<ProgrammeMembershipDTO> programmeMembershipDTOList = Lists.newArrayList(programmeMembershipDTO1, programmeMembershipDTO2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(new Long(5L), new Long(6L));

    when(programmeMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID)).thenReturn(programmeMemberships);
    when(programmeMembershipMapperMock.programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships)).thenReturn(programmeMembershipDTOList);
    when(curriculumRepositoryMock.findAll(curriculumIds)).thenReturn(foundCurricula);
    when(curriculumMapperMock.curriculumToCurriculumDTO(curriculum1)).thenReturn(curriculumDTO1);
    when(curriculumMapperMock.curriculumToCurriculumDTO(curriculum2)).thenReturn(curriculumDTO2);

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(2L, result.size());

    verify(programmeMembershipRepositoryMock).findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID);
    verify(programmeMembershipMapperMock).programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships);
    verify(curriculumRepositoryMock).findAll(curriculumIds);
    verify(curriculumMapperMock).curriculumToCurriculumDTO(curriculum1);
    verify(curriculumMapperMock).curriculumToCurriculumDTO(curriculum2);
  }


  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTrainee(null);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never()).findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
      verify(curriculumRepositoryMock, never()).findAll(anyList());
      verify(curriculumMapperMock, never()).curriculumToCurriculumDTO(any(Curriculum.class));
      verify(programmeRepositoryMock, never()).findByIdIn(anySet());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnEmptyListWhenNoResultsFound() {
    List<ProgrammeMembership> emptyProgrammeMembershipList = Lists.emptyList();
    List<ProgrammeMembershipDTO> emptyProgrammeMembershipDTOList = Lists.emptyList();
    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID)).thenReturn(emptyProgrammeMembershipList);
    when(programmeMembershipMapperMock.programmeMembershipsToProgrammeMembershipDTOs(emptyProgrammeMembershipList)).thenReturn(emptyProgrammeMembershipDTOList);

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0L, result.size());

    verify(programmeMembershipMapperMock, never()).programmeMembershipsToProgrammeMembershipDTOs(anyList());
    verify(curriculumRepositoryMock, never()).findAll(anyList());
    verify(curriculumMapperMock, never()).curriculumToCurriculumDTO(any(Curriculum.class));
    verify(programmeRepositoryMock, never()).findByIdIn(anySet());
    verify(programmeMembershipRepositoryMock).findByTraineeId(TRAINEE_ID);
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnPopulatedDTOList() {

    programmeMembershipDTO1.getCurriculumMemberships().add(curriculumMembershipDTO1);
    programmeMembershipDTO2.getCurriculumMemberships().add(curriculumMembershipDTO2);

    List<ProgrammeMembership> programmeMemberships = Lists.newArrayList(programmeMembership1, programmeMembership2);
    List<ProgrammeMembershipDTO> programmeMembershipDTOList = Lists.newArrayList(programmeMembershipDTO1, programmeMembershipDTO2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(new Long(5L), new Long(6L));

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID)).thenReturn(programmeMemberships);
    when(programmeMembershipMapperMock.allEntityToDto(programmeMemberships)).thenReturn(programmeMembershipDTOList);
    when(curriculumRepositoryMock.findAll(curriculumIds)).thenReturn(foundCurricula);
    when(curriculumMapperMock.curriculumToCurriculumDTO(curriculum1)).thenReturn(curriculumDTO1);
    when(curriculumMapperMock.curriculumToCurriculumDTO(curriculum2)).thenReturn(curriculumDTO2);
    when(programmeRepositoryMock.findByIdIn(Sets.newLinkedHashSet(PROGRAMME_ID))).thenReturn(Lists.newArrayList(programme));

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(2L, result.size());
    ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = result.get(0);
    Assert.assertEquals(PROGRAMME_NAME, programmeMembershipCurriculaDTO.getProgrammeName());
    Assert.assertEquals(PROGRAMME_NUMBER1, programmeMembershipCurriculaDTO.getProgrammeNumber());

    verify(programmeMembershipRepositoryMock).findByTraineeId(TRAINEE_ID);
    verify(programmeMembershipMapperMock).allEntityToDto(programmeMemberships);
    verify(curriculumRepositoryMock).findAll(curriculumIds);
    verify(curriculumMapperMock).curriculumToCurriculumDTO(curriculum1);
    verify(curriculumMapperMock).curriculumToCurriculumDTO(curriculum2);
    verify(programmeRepositoryMock).findByIdIn(Sets.newLinkedHashSet(PROGRAMME_ID));
  }
}