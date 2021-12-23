package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.mapper.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipServiceImplTest {

  private static final long TRAINEE_ID = 1L;
  private static final String TRAINEE_NUMBER = "XXX/XXX/XXX";

  private static final long PROGRAMME_ID = 2L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_1 = 7777L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_2 = 8888L;
  private static final String PROGRAMME_NUMBER1 = "Programme number";
  private static final String PROGRAMME_NAME = "Programme Name";
  private final ProgrammeMembership programmeMembership1 = new ProgrammeMembership();
  private final ProgrammeMembership programmeMembership2 = new ProgrammeMembership();
  private final ProgrammeMembershipDTO programmeMembershipDTO1 = new ProgrammeMembershipDTO();
  private final ProgrammeMembershipDTO programmeMembershipDTO2 = new ProgrammeMembershipDTO();
  private final Curriculum curriculum1 = new Curriculum();
  private final Curriculum curriculum2 = new Curriculum();
  private final CurriculumDTO curriculumDTO1 = new CurriculumDTO();
  private final CurriculumDTO curriculumDTO2 = new CurriculumDTO();
  private final TrainingNumber trainingNumber = new TrainingNumber();
  private final TrainingNumberDTO trainingNumberDTO = new TrainingNumberDTO();
  private final CurriculumMembershipDTO curriculumMembershipDTO1 = new CurriculumMembershipDTO();
  private final CurriculumMembershipDTO curriculumMembershipDTO2 = new CurriculumMembershipDTO();
  private final Programme programme = new Programme();
  private ProgrammeMembershipServiceImpl testObj;
  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private CurriculumMembershipRepository curriculumMembershipRepositoryMock;
  @Mock
  private CurriculumRepository curriculumRepositoryMock;
  @Mock
  private ProgrammeRepository programmeRepositoryMock;
  @Mock
  private ApplicationEventPublisher applicationEventPublisherMock;
  @Mock
  private PersonRepository personRepositoryMock;

  @Before
  public void setup() {
    ProgrammeMembershipMapper programmeMembershipMapper = new ProgrammeMembershipMapper();
    CurriculumMembershipMapper curriculumMembershipMapper = new CurriculumMembershipMapper();
    CurriculumMapper curriculumMapper = new CurriculumMapperImpl();
    ReflectionTestUtils.setField(curriculumMapper, "specialtyMapper",
        new SpecialtyMapperImpl());
    testObj = new ProgrammeMembershipServiceImpl(programmeMembershipRepositoryMock,
        curriculumMembershipRepositoryMock,
        programmeMembershipMapper, curriculumMembershipMapper, curriculumRepositoryMock, curriculumMapper,
        programmeRepositoryMock, applicationEventPublisherMock, personRepositoryMock);

    initialiseData();
  }

  private void initialiseData() {
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
      verify(programmeMembershipRepositoryMock, never())
          .findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(curriculumRepositoryMock, never()).findAllById(anyCollection());
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenProgrammeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, null);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never())
          .findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(curriculumRepositoryMock, never()).findAllById(anyCollection());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnEmptyListWhenNoResultsFound() {
    List<ProgrammeMembership> emptyProgrammeMembershipList = Lists.emptyList();
    when(programmeMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID))
        .thenReturn(emptyProgrammeMembershipList);

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnPopulatedDTO() {
    List<ProgrammeMembership> programmeMemberships = Lists
        .newArrayList(programmeMembership1, programmeMembership2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);

    when(programmeMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID))
        .thenReturn(programmeMemberships);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(foundCurricula);

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(2, result.get(0).getCurriculumMemberships().size());
  }


  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTrainee(null);
    } catch (Exception e) {
      verify(programmeMembershipRepositoryMock, never())
          .findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(curriculumRepositoryMock, never()).findAllById(anyCollection());
      verify(programmeRepositoryMock, never()).findByIdIn(anySet());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnEmptyListWhenNoResultsFound() {
    List<ProgrammeMembership> emptyProgrammeMembershipList = Lists.emptyList();
    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID))
        .thenReturn(emptyProgrammeMembershipList);

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnPopulatedDTOList() {
    List<ProgrammeMembership> programmeMemberships = Lists
        .newArrayList(programmeMembership1, programmeMembership2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(new Long(5L), new Long(6L));

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID))
        .thenReturn(programmeMemberships);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(foundCurricula);
    when(programmeRepositoryMock.findByIdIn(Sets.newLinkedHashSet(PROGRAMME_ID)))
        .thenReturn(Lists.newArrayList(programme));

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_1, result.get(0).getId());
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_2, result.get(1).getId());
    ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = result.get(0);
    Assert.assertEquals(PROGRAMME_NAME, programmeMembershipCurriculaDTO.getProgrammeName());
    Assert.assertEquals(PROGRAMME_NUMBER1, programmeMembershipCurriculaDTO.getProgrammeNumber());
  }


  @Test
  public void findProgrammeMembershipsForTraineeRolledUpShouldThrowExceptionWithWhenTraineeIdIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID);
    } catch (Exception e) {
      verify(testObj, never()).findProgrammeMembershipsForTrainee(anyLong());
      throw e;
    }
  }

  @Test
  public void findProgrammeMembershipsForTraineeRolledUpShouldRollUpPMs() {
    LocalDate dateFrom = LocalDate.of(1999, 12, 31);
    LocalDate dateTo = LocalDate.of(2000, 12, 31);
    LocalDate anotherDateFrom = LocalDate.of(2011, 12, 31);
    LocalDate anotherDateTo = LocalDate.of(2015, 12, 31);

    ProgrammeMembership pm1 = new ProgrammeMembership();
    ProgrammeMembership pm2 = new ProgrammeMembership();
    ProgrammeMembership pm3 = new ProgrammeMembership();

    pm1.setProgramme(programme);
    pm1.setProgrammeStartDate(dateFrom);
    pm1.setProgrammeEndDate(dateTo);
    pm1.setCurriculumId(curriculum1.getId());
    pm1.setCurriculumStartDate(dateFrom);
    pm1.setCurriculumEndDate(dateTo);
    pm1.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);

    pm2.setProgramme(programme);
    pm2.setProgrammeStartDate(dateFrom);
    pm2.setProgrammeEndDate(dateTo);
    pm2.setCurriculumId(curriculum1.getId());
    pm2.setCurriculumStartDate(anotherDateFrom);
    pm2.setCurriculumEndDate(anotherDateTo);
    pm2.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);

    pm3.setProgramme(programme);
    pm3.setProgrammeStartDate(anotherDateFrom);
    pm3.setProgrammeEndDate(anotherDateTo);
    pm3.setCurriculumId(curriculum2.getId());
    pm3.setCurriculumStartDate(anotherDateFrom);
    pm3.setCurriculumEndDate(anotherDateTo);
    pm3.setProgrammeMembershipType(ProgrammeMembershipType.ACADEMIC);

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID))
        .thenReturn(Lists.newArrayList(pm1, pm2, pm3));
    Set<Long> curriculumIds = Sets.newLinkedHashSet(Long.valueOf(5L), Long.valueOf(6L));
    when(curriculumRepositoryMock.findAllById(curriculumIds))
        .thenReturn(Lists.newArrayList(curriculum1, curriculum2));
    when(programmeRepositoryMock.findByIdIn(Sets.newLinkedHashSet(PROGRAMME_ID)))
        .thenReturn(Lists.newArrayList(programme));

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID);

    Assert.assertEquals(2, result.size());
    long dateFromCount = result.stream().filter(
        pmc -> pmc.getProgrammeStartDate().equals(dateFrom)).count();
    Assert.assertEquals(1L, dateFromCount);
    long anotherDateFromCount = result.stream()
        .filter(pmc -> pmc.getProgrammeStartDate().equals(anotherDateFrom)).count();
    Assert.assertEquals(1L, anotherDateFromCount);

    Optional<ProgrammeMembershipCurriculaDTO> any = result.stream()
        .filter(pmc -> pmc.getProgrammeStartDate().equals(dateFrom)).findAny();
    Assert.assertTrue(any.isPresent());
    ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = any.get();
    Assert.assertEquals(2,
        programmeMembershipCurriculaDTO.getCurriculumMemberships().size());
  }

  @Test
  public void testProgrammeMembershipWithCertificateType() {
    LocalDate pm1DateFrom = LocalDate.of(2019, 12, 31);
    LocalDate pm1DateTo = LocalDate.of(2020, 12, 31);
    LocalDate pm2DateFrom = LocalDate.of(2011, 12, 31);
    LocalDate pm2DateTo = LocalDate.of(2015, 12, 31);
    LocalDate pm3DateFrom = LocalDate.of(2017, 12, 31);
    LocalDate pm3DateTo = LocalDate.of(2019, 12, 31);

    ProgrammeMembership pm1 = new ProgrammeMembership();
    ProgrammeMembership pm2 = new ProgrammeMembership();
    ProgrammeMembership pm3 = new ProgrammeMembership();

    pm1.setProgramme(programme);
    pm1.setProgrammeStartDate(pm1DateFrom);
    pm1.setProgrammeEndDate(pm1DateTo);
    pm1.setCurriculumId(curriculum1.getId());
    pm1.setCurriculumStartDate(pm1DateFrom);
    pm1.setCurriculumEndDate(pm1DateTo);
    pm1.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);
    pm1.setTrainingPathway("CCT");

    pm2.setProgramme(programme);
    pm2.setProgrammeStartDate(pm2DateFrom);
    pm2.setProgrammeEndDate(pm2DateTo);
    pm2.setCurriculumId(curriculum1.getId());
    pm2.setCurriculumStartDate(pm2DateFrom);
    pm2.setCurriculumEndDate(pm2DateTo);
    pm2.setProgrammeMembershipType(ProgrammeMembershipType.SUBSTANTIVE);
    pm2.setTrainingPathway("CESR");

    pm3.setProgramme(programme);
    pm3.setProgrammeStartDate(pm3DateFrom);
    pm3.setProgrammeEndDate(pm3DateTo);
    pm3.setCurriculumId(curriculum2.getId());
    pm3.setCurriculumStartDate(pm3DateFrom);
    pm3.setCurriculumEndDate(pm3DateTo);
    pm3.setProgrammeMembershipType(ProgrammeMembershipType.ACADEMIC);
    pm3.setTrainingPathway("CESR");

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID)).thenReturn(
        Lists.newArrayList(pm1, pm2, pm3));
    Set<Long> curriculumIds = Sets.newLinkedHashSet(Long.valueOf(5L), Long.valueOf(6L));
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(
        Lists.newArrayList(curriculum1, curriculum2));
    when(programmeRepositoryMock.findByIdIn(Sets.newLinkedHashSet(PROGRAMME_ID)))
        .thenReturn(Lists.newArrayList(programme));

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeRolledUp(TRAINEE_ID);

    long cctCount = result.stream().filter(pm -> pm.getTrainingPathway().equals("CCT")).count();
    Assert.assertEquals(1L, cctCount);

    long cesrCount = result.stream().filter(pm -> pm.getTrainingPathway().equals("CESR")).count();
    Assert.assertEquals(2L, cesrCount);

    long nullCount = result.stream().filter(pm -> pm.getTrainingPathway().equals(null)).count();
    Assert.assertEquals(0L, nullCount);
  }

  @Test
  public void shouldFindProgrammeMembershipDetailsByIds() {
    Set<Long> ids = new HashSet<>();
    ids.add(1L);
    ids.add(2L);

    programmeMembership1.setCurriculumId(curriculum1.getId());
    programmeMembership2.setCurriculumId(curriculum2.getId());

    List<ProgrammeMembership> programmeMemberships = Lists
        .newArrayList(programmeMembership1, programmeMembership2);

    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(foundCurricula);

    when(programmeMembershipRepositoryMock.findByIdIn(ids)).thenReturn(programmeMemberships);

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipDetailsByIds(ids);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());

    ProgrammeMembershipCurriculaDTO pmc1 = result.get(0);
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_1, pmc1.getId());
    Assert.assertEquals(PROGRAMME_ID, pmc1.getProgrammeId().longValue());
    Assert.assertEquals(curriculum1.getId(), pmc1.getCurriculumDTO().getId());

    ProgrammeMembershipCurriculaDTO pmc2 = result.get(1);
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_2, pmc2.getId());
    Assert.assertEquals(PROGRAMME_ID, pmc2.getProgrammeId().longValue());
    Assert.assertEquals(curriculum2.getId(), pmc2.getCurriculumDTO().getId());
  }
}
