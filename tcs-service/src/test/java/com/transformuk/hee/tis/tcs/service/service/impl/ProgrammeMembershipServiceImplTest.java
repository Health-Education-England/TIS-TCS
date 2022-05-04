package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapperImpl;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapperImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeMembershipServiceImplTest {

  private static final long TRAINEE_ID = 1L;
  private static final String TRAINEE_NUMBER = "XXX/XXX/XXX";
  private static final long CURRICULUM_1_ID = 5L;
  private static final long PROGRAMME_ID = 2L;
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final Long PROGRAMME_MEMBERSHIP_ID_1 = 11L;
  private static final Long PROGRAMME_MEMBERSHIP_ID_2 = 22L;
  private static final Long CURRICULUM_MEMBERSHIP_ID_1 = 7777L;
  private static final Long CURRICULUM_MEMBERSHIP_ID_2 = 8888L;
  private static final LocalDate PROGRAMME_START_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDate PROGRAMME_END_DATE = LocalDate.of(2022, 1, 1);
  private static final String PROGRAMME_NUMBER1 = "Programme number";
  private static final String PROGRAMME_NAME = "Programme Name";
  private final CurriculumMembership curriculumMembership1 = new CurriculumMembership();
  private final CurriculumMembership curriculumMembership2 = new CurriculumMembership();
  private final ProgrammeMembership programmeMembership1 = new ProgrammeMembership();
  private final Curriculum curriculum1 = new Curriculum();
  private final Curriculum curriculum2 = new Curriculum();
  private final CurriculumDTO curriculumDto1 = new CurriculumDTO();
  private final CurriculumDTO curriculumDto2 = new CurriculumDTO();
  private final TrainingNumber trainingNumber = new TrainingNumber();
  private final TrainingNumberDTO trainingNumberDto = new TrainingNumberDTO();
  private ProgrammeMembershipDTO programmeMembershipDto1 = new ProgrammeMembershipDTO();
  private final Programme programme = new Programme();
  private final PersonDTO personDto = new PersonDTO();
  private final Person person = new Person();
  private ProgrammeMembershipServiceImpl testObj;
  private ProgrammeMembershipMapper programmeMembershipMapper;
  private CurriculumMembershipMapper curriculumMembershipMapper;
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
    curriculumMembershipMapper = new CurriculumMembershipMapper();
    programmeMembershipMapper = new ProgrammeMembershipMapper(curriculumMembershipMapper);
    CurriculumMapper curriculumMapper = new CurriculumMapperImpl();
    ReflectionTestUtils.setField(curriculumMapper, "specialtyMapper",
        new SpecialtyMapperImpl());
    testObj = new ProgrammeMembershipServiceImpl(programmeMembershipRepositoryMock, curriculumMembershipRepositoryMock,
        programmeMembershipMapper, curriculumMembershipMapper, curriculumRepositoryMock, curriculumMapper,
        programmeRepositoryMock, applicationEventPublisherMock, personRepositoryMock);

    initialiseData();
  }

  private void initialiseData() {
    person.setId(1L);
    person.setCurriculumMemberships(Sets.newHashSet());

    trainingNumber.setId(TRAINEE_ID);
    trainingNumber.setTrainingNumber(TRAINEE_NUMBER);

    programme.setId(PROGRAMME_ID);
    programme.setProgrammeNumber(PROGRAMME_NUMBER1);
    programme.setProgrammeName(PROGRAMME_NAME);

    curriculumMembership1.setId(CURRICULUM_MEMBERSHIP_ID_1);
    curriculumMembership1.setCurriculumId(5L);
    curriculumMembership1.setProgrammeMembership(programmeMembership1);

    curriculumMembership2.setId(CURRICULUM_MEMBERSHIP_ID_2);
    curriculumMembership2.setCurriculumId(6L);
    curriculumMembership2.setProgrammeMembership(programmeMembership1);

    programmeMembership1.setId(PROGRAMME_MEMBERSHIP_ID_1);
    programmeMembership1.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembership1.setProgramme(programme);
    programmeMembership1.setProgrammeStartDate(PROGRAMME_START_DATE);
    programmeMembership1.setProgrammeEndDate(PROGRAMME_END_DATE);
    programmeMembership1.setPerson(person);
    programmeMembership1.setTrainingNumber(trainingNumber);
    programmeMembership1.setCurriculumMemberships(Sets.newLinkedHashSet(curriculumMembership1, curriculumMembership2));

    trainingNumberDto.setId(TRAINEE_ID);
    trainingNumberDto.setTrainingNumber(TRAINEE_NUMBER);

    personDto.setId(1L);
    personDto.setStatus(Status.INACTIVE);
    personDto.setProgrammeMemberships(Sets.newHashSet());

    programmeMembershipDto1 = programmeMembershipMapper.toDto(programmeMembership1);

    curriculum1.setId(5L);
    curriculum1.setName("XXX");
    curriculum2.setId(6L);
    curriculum2.setName("YYY");

    curriculumDto1.setId(5L);
    curriculumDto1.setName("XXX");
    curriculumDto2.setId(6L);
    curriculumDto2.setName("YYY");
  }

  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTraineeAndProgramme(null, PROGRAMME_ID);
    } catch (Exception e) {
      verify(curriculumMembershipRepositoryMock, never())
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
      verify(curriculumMembershipRepositoryMock, never())
          .findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(curriculumRepositoryMock, never()).findAllById(anyCollection());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnEmptyListWhenNoResultsFound() {
    List<CurriculumMembership> emptyCurriculumMembershipList = Lists.emptyList();
    when(curriculumMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID))
        .thenReturn(emptyCurriculumMembershipList);

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Test()
  public void findProgrammeMembershipsForTraineeAndProgrammeShouldReturnPopulatedDTO() {
    List<CurriculumMembership> curriculumMemberships = Lists
        .newArrayList(curriculumMembership1, curriculumMembership2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);

    when(curriculumMembershipRepositoryMock.findByTraineeIdAndProgrammeId(TRAINEE_ID, PROGRAMME_ID))
        .thenReturn(curriculumMemberships);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(foundCurricula);

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTraineeAndProgramme(TRAINEE_ID, PROGRAMME_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(2, result.get(0).getCurriculumMemberships().size());
  }

  @Test()
  public void findAllShouldReturnPageOfPopulatedDTOs() {
    //given
    List<CurriculumMembership> curriculumMemberships = Lists
        .newArrayList(curriculumMembership1, curriculumMembership2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);
    Page<CurriculumMembership> curriculumMembershipPage = new PageImpl<>(curriculumMemberships);
    when(curriculumMembershipRepositoryMock.findAll(any(Pageable.class)))
        .thenReturn(curriculumMembershipPage);
    Pageable pageable = PageRequest.of(1, 10);

    //when
    Page<ProgrammeMembershipDTO> result = testObj.findAll(pageable);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(curriculumMemberships.size(), result.toList().size());
    Assert.assertEquals(1, result.toList().get(0).getCurriculumMemberships().size());
  }

  @Test()
  public void findOneShouldReturnProgrammeMembershipDTO() {
    //given
    when(curriculumMembershipRepositoryMock.findById(1L))
        .thenReturn(Optional.of(curriculumMembership1));

    //when
    ProgrammeMembershipDTO result = testObj.findOne(1L);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(PROGRAMME_ID, result.getProgrammeId().longValue());
    Assert.assertEquals(1, result.getCurriculumMemberships().size());
  }


  @Test(expected = NullPointerException.class)
  public void findProgrammeMembershipsForTraineeShouldFailWhenTraineeIsNull() {
    try {
      testObj.findProgrammeMembershipsForTrainee(null);
    } catch (Exception e) {
      verify(curriculumMembershipRepositoryMock, never())
          .findByTraineeIdAndProgrammeId(anyLong(), anyLong());
      verify(curriculumRepositoryMock, never()).findAllById(anyCollection());
      verify(programmeRepositoryMock, never()).findByIdIn(anySet());
      throw e;
    }
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnEmptyListWhenNoResultsFound() {

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.size());
  }

  @Test()
  public void findProgrammeMembershipsForTraineeShouldReturnPopulatedDTOList() {
    List<CurriculumMembership> curriculumMemberships = Lists
        .newArrayList(curriculumMembership1, curriculumMembership2);
    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID))
        .thenReturn(Collections.singletonList(programmeMembership1));

    List<ProgrammeMembershipCurriculaDTO> result = testObj
        .findProgrammeMembershipsForTrainee(TRAINEE_ID);

    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_1, result.get(0).getId());
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
    //TODO: vary no. of CMs per PM
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
    pm1.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);

    pm2.setProgramme(programme);
    pm2.setProgrammeStartDate(dateFrom);
    pm2.setProgrammeEndDate(dateTo);
    pm2.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);

    pm3.setProgramme(programme);
    pm3.setProgrammeStartDate(anotherDateFrom);
    pm3.setProgrammeEndDate(anotherDateTo);
    pm3.setProgrammeMembershipType(ProgrammeMembershipType.ACADEMIC);

    CurriculumMembership cm1 = new CurriculumMembership();
    CurriculumMembership cm2 = new CurriculumMembership();
    CurriculumMembership cm3 = new CurriculumMembership();

    cm1.setCurriculumId(curriculum1.getId());
    cm1.setCurriculumStartDate(dateFrom);
    cm1.setCurriculumEndDate(dateTo);
    cm1.setProgrammeMembership(pm1);

    cm2.setCurriculumId(curriculum1.getId());
    cm2.setCurriculumStartDate(anotherDateFrom);
    cm2.setCurriculumEndDate(anotherDateTo);
    cm2.setProgrammeMembership(pm2);

    cm3.setCurriculumId(curriculum2.getId());
    cm3.setCurriculumStartDate(anotherDateFrom);
    cm3.setCurriculumEndDate(anotherDateTo);
    cm3.setProgrammeMembership(pm3);

    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2));
    pm3.setCurriculumMemberships(Sets.newLinkedHashSet(cm3));

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID))
        .thenReturn(Lists.newArrayList(pm1, pm2, pm3));
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);
    when(curriculumRepositoryMock.findAllById(curriculumIds))
        .thenReturn(Lists.newArrayList(curriculum1, curriculum2));

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
    //TODO: vary no. of CMs per PM
    LocalDate pm1DateFrom = LocalDate.of(2019, 12, 31);
    LocalDate pm1DateTo = LocalDate.of(2020, 12, 31);
    LocalDate pm2DateFrom = LocalDate.of(2011, 12, 31);
    LocalDate pm2DateTo = LocalDate.of(2015, 12, 31);
    LocalDate pm3DateFrom = LocalDate.of(2017, 12, 31);
    LocalDate pm3DateTo = LocalDate.of(2019, 12, 31);

    CurriculumMembership cm1 = new CurriculumMembership();
    CurriculumMembership cm2 = new CurriculumMembership();
    CurriculumMembership cm3 = new CurriculumMembership();

    ProgrammeMembership pm1 = new ProgrammeMembership();
    ProgrammeMembership pm2 = new ProgrammeMembership();
    ProgrammeMembership pm3 = new ProgrammeMembership();

    pm1.setProgramme(programme);
    pm1.setProgrammeStartDate(pm1DateFrom);
    pm1.setProgrammeEndDate(pm1DateTo);
    pm1.setProgrammeMembershipType(ProgrammeMembershipType.FTSTA);
    pm1.setTrainingPathway("CCT");

    pm2.setProgramme(programme);
    pm2.setProgrammeStartDate(pm2DateFrom);
    pm2.setProgrammeEndDate(pm2DateTo);
    pm2.setProgrammeMembershipType(ProgrammeMembershipType.SUBSTANTIVE);
    pm2.setTrainingPathway("CESR");

    pm3.setProgramme(programme);
    pm3.setProgrammeStartDate(pm3DateFrom);
    pm3.setProgrammeEndDate(pm3DateTo);
    pm3.setProgrammeMembershipType(ProgrammeMembershipType.ACADEMIC);
    pm3.setTrainingPathway("CESR");

    cm1.setCurriculumId(curriculum1.getId());
    cm1.setCurriculumStartDate(pm1DateFrom);
    cm1.setCurriculumEndDate(pm1DateTo);
    cm1.setProgrammeMembership(pm1);

    cm2.setCurriculumId(curriculum1.getId());
    cm2.setCurriculumStartDate(pm2DateFrom);
    cm2.setCurriculumEndDate(pm2DateTo);
    cm2.setProgrammeMembership(pm2);

    cm3.setCurriculumId(curriculum2.getId());
    cm3.setCurriculumStartDate(pm3DateFrom);
    cm3.setCurriculumEndDate(pm3DateTo);
    cm3.setProgrammeMembership(pm3);

    pm1.setCurriculumMemberships(Sets.newLinkedHashSet(cm1));
    pm2.setCurriculumMemberships(Sets.newLinkedHashSet(cm2));
    pm3.setCurriculumMemberships(Sets.newLinkedHashSet(cm3));

    when(programmeMembershipRepositoryMock.findByTraineeId(TRAINEE_ID)).thenReturn(
        Lists.newArrayList(pm1, pm2, pm3));
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(
        Lists.newArrayList(curriculum1, curriculum2));

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

    curriculumMembership1.setCurriculumId(curriculum1.getId());
    curriculumMembership2.setCurriculumId(curriculum2.getId());

    List<CurriculumMembership> curriculumMemberships = Lists
        .newArrayList(curriculumMembership1, curriculumMembership2);

    List<Curriculum> foundCurricula = Lists.newArrayList(curriculum1, curriculum2);
    Set<Long> curriculumIds = Sets.newLinkedHashSet(5L, 6L);
    when(curriculumRepositoryMock.findAllById(curriculumIds)).thenReturn(foundCurricula);

    when(curriculumMembershipRepositoryMock.findByIdIn(ids)).thenReturn(curriculumMemberships);

    List<ProgrammeMembershipCurriculaDTO> result = testObj.findProgrammeMembershipDetailsByIds(ids);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());

    ProgrammeMembershipCurriculaDTO pmc1 = result.get(0);
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_1, pmc1.getId());
    Assert.assertEquals(PROGRAMME_ID, pmc1.getProgrammeId().longValue());
    Assert.assertEquals(curriculum1.getId(), pmc1.getCurriculumDTO().getId());

    ProgrammeMembershipCurriculaDTO pmc2 = result.get(1);
    Assert.assertEquals(PROGRAMME_MEMBERSHIP_ID_1, pmc2.getId());
    Assert.assertEquals(PROGRAMME_ID, pmc2.getProgrammeId().longValue());
    Assert.assertEquals(curriculum2.getId(), pmc2.getCurriculumDTO().getId());
  }

  @Test
  public void shouldSaveProgrammeMembershipDtoToRepository() {
    //given
    List<CurriculumMembership> curriculumMembershipList
        = new ArrayList<>(programmeMembership1.getCurriculumMemberships());

    when(programmeMembershipRepositoryMock.save(any()))
        .thenReturn(programmeMembership1);
    when(personRepositoryMock.getOne(anyLong())).thenReturn(person);

    //when
    ProgrammeMembershipDTO programmeMembershipDTO = testObj.save(programmeMembershipDto1);

    //then
    verify(programmeMembershipRepositoryMock, times(1))
        .save(any());
    Assert.assertEquals(PROGRAMME_ID, programmeMembershipDTO.getProgrammeId().longValue());
    Assert.assertEquals(CURRICULUM_1_ID, programmeMembershipDTO.getCurriculumMemberships()
            .get(0).getCurriculumId().longValue());
  }

  @Test
  public void shouldSaveProgrammeMembershipDtoListToRepositories() {
    //given
    when(programmeMembershipRepositoryMock.save(any()))
        .thenReturn(programmeMembership1);
    when(personRepositoryMock.getOne(anyLong())).thenReturn(person);

    //when
    List<ProgrammeMembershipDTO> programmeMembershipDTOList =
        testObj.save(Lists.newArrayList(programmeMembershipDto1));

    //then
    verify(programmeMembershipRepositoryMock, times(1))
        .save(any()); //only 1 call since only 1 element in list
    Assert.assertEquals(PROGRAMME_ID, programmeMembershipDTOList.get(0)
        .getProgrammeId().longValue());
    Assert.assertEquals(CURRICULUM_1_ID, programmeMembershipDTOList.get(0)
        .getCurriculumMemberships().get(0).getCurriculumId().longValue());
  }

  @Test
  public void shouldDeleteCurriculumMemberships() {
    //given
    when(personRepositoryMock.getOne(anyLong())).thenReturn(person);
    when(curriculumMembershipRepositoryMock.getOne(CURRICULUM_MEMBERSHIP_ID_1))
        .thenReturn(curriculumMembership1);
    when(curriculumMembershipRepositoryMock.getOne(CURRICULUM_MEMBERSHIP_ID_2))
        .thenReturn(curriculumMembership2);

    //when
    testObj.delete(CURRICULUM_MEMBERSHIP_ID_1);

    //then
    verify(programmeMembershipRepositoryMock, times(1))
        .save(any()); //since there were 2 CMs, so the PM is updated not deleted

    testObj.delete(CURRICULUM_MEMBERSHIP_ID_2);

    verify(programmeMembershipRepositoryMock, times(1))
        .delete(any()); //since the last CM is deleted, so the PM is deleted too

  }


}
