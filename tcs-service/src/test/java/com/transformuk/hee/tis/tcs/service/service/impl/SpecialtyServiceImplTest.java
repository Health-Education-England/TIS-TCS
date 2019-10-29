package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class SpecialtyServiceImplTest {

  private static final String INTREPID_ID = "12345";
  private static final String NHS_CODE = "NHS_CODE";
  private static final String COLLEGE = "A MEDIA COLLEGE";
  private static final SpecialtyType SUB_SPECIALTY = SpecialtyType.SUB_SPECIALTY;

  @InjectMocks
  private SpecialtyServiceImpl testObj;

  @Mock
  private SpecialtyRepository specialtyRepositoryMock;

  @Mock
  private SpecialtyMapper specialtyMapperMock;

  @Mock
  private Specialty specialtyMock;

  @Captor
  private ArgumentCaptor<Status> statusCaptor;

  private SpecialtyDTO specialtyDTO;

  @Mock
  private ApplicationEventPublisher applicationEventPublisherMock;

  @Before
  public void setup() {
    specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setSpecialtyGroup(new SpecialtyGroupDTO());
    specialtyDTO.setIntrepidId(INTREPID_ID);
    specialtyDTO.setSpecialtyTypes(Sets.newHashSet(SUB_SPECIALTY));
    specialtyDTO.setSpecialtyCode(NHS_CODE);
    specialtyDTO.setCollege(COLLEGE);
    specialtyDTO.setStatus(Status.INACTIVE);
  }

  @Test
  public void saveShouldSaveSpecialty() {
    SpecialtyDTO expectedSpecialtyDTO = new SpecialtyDTO();
    expectedSpecialtyDTO.setId(12345L);
    when(specialtyMapperMock.specialtyDTOToSpecialty(specialtyDTO)).thenReturn(specialtyMock);
    when(specialtyRepositoryMock.save(specialtyMock)).thenReturn(specialtyMock);
    when(specialtyMapperMock.specialtyToSpecialtyDTO(specialtyMock))
        .thenReturn(expectedSpecialtyDTO);

    SpecialtyDTO result = testObj.save(specialtyDTO);

    Assert.assertEquals(expectedSpecialtyDTO, result);
    verify(specialtyMapperMock).specialtyDTOToSpecialty(specialtyDTO);
    verify(specialtyRepositoryMock).save(specialtyMock);
    verify(specialtyMapperMock).specialtyToSpecialtyDTO(specialtyMock);
  }

  @Test
  public void saveShouldSaveSpecialtyWithDefaultStatusWhenItsNull() {
    SpecialtyDTO expectedSpecialtyDTO = new SpecialtyDTO();
    expectedSpecialtyDTO.setId(12345L);
    //clear the status to null
    specialtyDTO.setStatus(null);

    when(specialtyMapperMock.specialtyDTOToSpecialty(specialtyDTO)).thenReturn(specialtyMock);
    when(specialtyRepositoryMock.save(specialtyMock)).thenReturn(specialtyMock);
    when(specialtyMapperMock.specialtyToSpecialtyDTO(specialtyMock))
        .thenReturn(expectedSpecialtyDTO);

    SpecialtyDTO result = testObj.save(specialtyDTO);

    Assert.assertEquals(expectedSpecialtyDTO, result);
    verify(specialtyMapperMock).specialtyDTOToSpecialty(specialtyDTO);
    verify(specialtyMock).setStatus(statusCaptor.capture());

    Status capturedStatus = statusCaptor.getValue();
    Assert.assertEquals(Status.CURRENT, capturedStatus);

    verify(specialtyRepositoryMock).save(specialtyMock);
    verify(specialtyMapperMock).specialtyToSpecialtyDTO(specialtyMock);
  }

  @Test
  public void getPagedSpecialtiesForProgrammeIdShouldReturnListOfSpecialtiesForProgramme() {
    int page = 0;
    PageRequest pageRequest = PageRequest.of(page, 10);
    Long programmeId = 1L;
    List<Specialty> foundSpecialties = Lists.newArrayList(specialtyMock);
    PageImpl<Specialty> pagedSpecialties = new PageImpl<>(foundSpecialties, pageRequest, 1L);
    List<SpecialtyDTO> convertedSpecialties = Lists.newArrayList(specialtyDTO);

    when(specialtyRepositoryMock
        .findSpecialtiesByProgrammeId(programmeId, Status.CURRENT, pageRequest))
        .thenReturn(pagedSpecialties);
    when(specialtyMapperMock.specialtiesToSpecialtyDTOs(foundSpecialties))
        .thenReturn(convertedSpecialties);

    Page<SpecialtyDTO> result = testObj
        .getPagedSpecialtiesForProgrammeId(programmeId, StringUtils.EMPTY, pageRequest);

    Assert.assertEquals(1L, result.getNumberOfElements());
    Assert.assertEquals(page, result.getNumber());
    Assert.assertEquals(1L, result.getContent().size());
    Assert.assertEquals(specialtyDTO, result.getContent().get(0));
  }

  @Test
  public void getPagedSpecialtiesForProgrammeIdShouldReturnEmptyPageWhenNoSpecialtiesFound() {
    int page = 0;
    PageRequest pageRequest = PageRequest.of(page, 10);
    Long programmeId = 1L;
    List<Specialty> noFoundSpecialties = Lists.newArrayList();
    PageImpl<Specialty> pagedSpecialties = new PageImpl<>(noFoundSpecialties, pageRequest, 1L);
    List<SpecialtyDTO> convertedSpecialties = Lists.newArrayList();

    when(specialtyRepositoryMock
        .findSpecialtiesByProgrammeId(programmeId, Status.CURRENT, pageRequest))
        .thenReturn(pagedSpecialties);
    when(specialtyMapperMock.specialtiesToSpecialtyDTOs(noFoundSpecialties))
        .thenReturn(convertedSpecialties);

    Page<SpecialtyDTO> result = testObj
        .getPagedSpecialtiesForProgrammeId(programmeId, StringUtils.EMPTY, pageRequest);

    Assert.assertEquals(0L, result.getNumberOfElements());
    Assert.assertEquals(page, result.getNumber());
    Assert.assertEquals(0L, result.getContent().size());
  }


  @Test(expected = NullPointerException.class)
  public void getPagedSpecialtiesForProgrammeIdShouldThrowExceptionWhenProgrammeIdIsNull() {
    try {
      testObj.getPagedSpecialtiesForProgrammeId(null, StringUtils.EMPTY, PageRequest.of(0, 10));
    } catch (Exception e) {
      verifyNoMoreInteractions(specialtyRepositoryMock);
      verifyNoMoreInteractions(specialtyMapperMock);
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void getPagedSpecialtiesForProgrammeIdShouldThrowExceptionWhenPageableIsNull() {
    try {
      testObj.getPagedSpecialtiesForProgrammeId(1L, StringUtils.EMPTY, null);
    } catch (Exception e) {
      verifyNoMoreInteractions(specialtyRepositoryMock);
      verifyNoMoreInteractions(specialtyMapperMock);
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void getPagedSpecialtiesForProgrammeIdShouldThrowExceptionWhenProgrammeIdAndPageableIsNull() {
    try {
      testObj.getPagedSpecialtiesForProgrammeId(null, StringUtils.EMPTY, null);
    } catch (Exception e) {
      verifyNoMoreInteractions(specialtyRepositoryMock);
      verifyNoMoreInteractions(specialtyMapperMock);
      throw e;
    }
  }

  @Test
  public void getPagedSpecialtiesForProgrammeIdShouldFindSpecialtiesWithNameLikeQuery() {
    int page = 0;
    PageRequest pageRequest = PageRequest.of(page, 10);
    Long programmeId = 1L;
    List<Specialty> foundSpecialties = Lists.newArrayList(specialtyMock);
    PageImpl<Specialty> pagedSpecialties = new PageImpl<>(foundSpecialties, pageRequest, 1L);
    List<SpecialtyDTO> convertedSpecialties = Lists.newArrayList(specialtyDTO);
    String searchCriteria = "SEARCH TEXT";

    when(specialtyRepositoryMock
        .findSpecialtiesByProgrammeIdAndName(programmeId, searchCriteria, Status.CURRENT,
            pageRequest)).thenReturn(pagedSpecialties);
    when(specialtyMapperMock.specialtiesToSpecialtyDTOs(foundSpecialties))
        .thenReturn(convertedSpecialties);

    Page<SpecialtyDTO> result = testObj
        .getPagedSpecialtiesForProgrammeId(programmeId, searchCriteria, pageRequest);

    Assert.assertEquals(1L, result.getNumberOfElements());
    Assert.assertEquals(page, result.getNumber());
    Assert.assertEquals(1L, result.getContent().size());
    Assert.assertEquals(specialtyDTO, result.getContent().get(0));

    verify(specialtyRepositoryMock, never())
        .findSpecialtyDistinctByCurriculaProgrammesProgrammeIdAndStatusIs(any(), any(), any());
  }

  @Test(expected = NullPointerException.class)
  public void getSpecialtiesForProgrammeAndPersonShouldThrowExceptionWhenProgrammeIdIsNull() {
    try {
      testObj.getSpecialtiesForProgrammeAndPerson(null, 1L);
    } catch (Exception e) {
      verifyZeroInteractions(specialtyRepositoryMock);
      verifyZeroInteractions(specialtyMapperMock);
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void getSpecialtiesForProgrammeAndPersonShouldThrowExceptionWhenPersonIdIsNull() {
    try {
      testObj.getSpecialtiesForProgrammeAndPerson(1L, null);
    } catch (Exception e) {
      verifyZeroInteractions(specialtyRepositoryMock);
      verifyZeroInteractions(specialtyMapperMock);
      throw e;
    }
  }

  @Test
  public void getSpecialtiesForProgrammeAndPersonShouldReturnEmptyListWhenNoSpecialtiesFound() {
    long programmeId = 1L;
    long personId = 2L;
    when(specialtyRepositoryMock
        .findDistinctByProgrammeIdAndPersonIdAndStatus(programmeId, personId, Status.CURRENT))
        .thenReturn(Collections.EMPTY_SET);
    List<SpecialtyDTO> result = testObj.getSpecialtiesForProgrammeAndPerson(programmeId, personId);

    Assert.assertEquals(0, result.size());

    verify(specialtyRepositoryMock)
        .findDistinctByProgrammeIdAndPersonIdAndStatus(programmeId, personId, Status.CURRENT);
    verify(specialtyMapperMock).specialtiesToSpecialtyDTOs(Collections.EMPTY_LIST);
  }

  @Test
  public void getSpecialtiesForProgrammeAndPersonShouldReturnSpecialtiesDTOFound() {
    long programmeId = 1L;
    long personId = 2L;
    Set<Specialty> foundSpecialties = Sets.newHashSet(specialtyMock);
    List<SpecialtyDTO> convertedSpecialties = Lists.newArrayList(specialtyDTO);

    when(specialtyRepositoryMock
        .findDistinctByProgrammeIdAndPersonIdAndStatus(programmeId, personId, Status.CURRENT))
        .thenReturn(foundSpecialties);
    List<Specialty> specialtiesList = Lists.newArrayList(foundSpecialties);
    when(specialtyMapperMock.specialtiesToSpecialtyDTOs(specialtiesList))
        .thenReturn(convertedSpecialties);

    List<SpecialtyDTO> result = testObj.getSpecialtiesForProgrammeAndPerson(programmeId, personId);

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(convertedSpecialties, result);

    verify(specialtyRepositoryMock)
        .findDistinctByProgrammeIdAndPersonIdAndStatus(programmeId, personId, Status.CURRENT);
    verify(specialtyMapperMock).specialtiesToSpecialtyDTOs(specialtiesList);
  }
}
