package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import com.transformuk.hee.tis.tcs.service.repository.QualificationRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.QualificationMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QualificationServiceImplTest {

  private static final Long PERSON_ID = 12345L;
  public static final long QUALIFICATION_ID = 1L;
  @InjectMocks
  private QualificationServiceImpl testObj;

  @Mock
  private QualificationRepository qualificationRepository;
  @Mock
  private QualificationMapper qualificationMapper;

  @Captor
  private ArgumentCaptor<Example<Qualification>> exampleArgumentCaptor;
  @Captor
  private ArgumentCaptor<Converter> converterArgumentCaptor;

  @Before
  public void setup() {
    this.testObj = new QualificationServiceImpl(qualificationRepository, qualificationMapper);
  }

  @Test
  public void findPersonQualificationShouldCallRepositoryWithExampleOfPersonWithId() {
    Qualification q1 = new Qualification(), q2 = new Qualification();
    List<Qualification> foundQualifications = Lists.newArrayList(q1, q2);

    QualificationDTO dto1 = new QualificationDTO(), dto2 = new QualificationDTO();
    List<QualificationDTO> foundQualificationDTOs = Lists.newArrayList(dto1, dto2);

    when(qualificationRepository.findAll(exampleArgumentCaptor.capture())).thenReturn(foundQualifications);
    when(qualificationMapper.toDTOs(foundQualifications)).thenReturn(foundQualificationDTOs);

    List<QualificationDTO> result = testObj.findPersonQualifications(PERSON_ID);

    Assert.assertEquals(foundQualificationDTOs.size(), result.size());
    Assert.assertEquals(foundQualificationDTOs.get(0), result.get(0));
    Assert.assertEquals(foundQualificationDTOs.get(1), result.get(1));

    Example<Qualification> exampleArgumentCaptorValue = exampleArgumentCaptor.getValue();
    Qualification capturedQualification = exampleArgumentCaptorValue.getProbe();

    Assert.assertEquals(PERSON_ID, capturedQualification.getPerson().getId());
  }

  @Test(expected = NullPointerException.class)
  public void findPersonQualificationsShouldThrowExceptionWhenPersonIdIsNull() {
    try {
      testObj.findPersonQualifications(null);
    } catch (Exception e) {
      verify(qualificationRepository, never()).findAll(any(Example.class));
      verify(qualificationMapper, never()).toDTOs(anyList());
      throw e;
    }
  }

  @Test
  public void deleteShouldCallDeleteOnRepository() {
    testObj.delete(QUALIFICATION_ID);

    verify(qualificationRepository, times(1)).delete(QUALIFICATION_ID);
  }

  @Test
  public void findOneShouldReturnTheDtoOfTheFoundEntity() {
    Qualification foundEntity = new Qualification();
    QualificationDTO dtoOfEntity = new QualificationDTO();

    when(qualificationRepository.findOne(QUALIFICATION_ID)).thenReturn(foundEntity);
    when(qualificationMapper.toDto(foundEntity)).thenReturn(dtoOfEntity);

    QualificationDTO result = testObj.findOne(QUALIFICATION_ID);

    Assert.assertEquals(dtoOfEntity, result);
  }

  @Test
  public void foundOneShouldReturnNullIfNoEntityFoundWithId() {
    when(qualificationRepository.findOne(QUALIFICATION_ID)).thenReturn(null);
    when(qualificationMapper.toDto(null)).thenReturn(null);

    QualificationDTO result = testObj.findOne(QUALIFICATION_ID);

    Assert.assertNull(result);
  }

  @Test
  public void findAllShouldSearchUsingAPageableAndReturnDtos() {
    Pageable pageableMock = mock(Pageable.class);
    Page<Qualification> pageMock = mock(Page.class);
    Page<QualificationDTO> dtoPageMock = mock(Page.class);


    when(qualificationRepository.findAll(pageableMock)).thenReturn(pageMock);
    when(pageMock.map(converterArgumentCaptor.capture())).thenReturn(dtoPageMock);

    Page<QualificationDTO> result = testObj.findAll(pageableMock);

    Assert.assertEquals(dtoPageMock, result);
  }

  @Test
  public void saveShouldConvertDtosAndSaveEntities() {
    QualificationDTO dto1 = new QualificationDTO(), dto2 = new QualificationDTO();
    List<QualificationDTO> toSave = Lists.newArrayList(dto1, dto2);

    Qualification q1 = new Qualification(), q2 = new Qualification();
    List<Qualification> convertedDtos = Lists.newArrayList(q1, q2);

    Qualification q3 = new Qualification(), q4 = new Qualification();
    List<Qualification> savedEntities = Lists.newArrayList(q1, q2);

    QualificationDTO dto3 = new QualificationDTO(), dto4 = new QualificationDTO();
    List<QualificationDTO> savedAndConvertedDtos = Lists.newArrayList(dto1, dto2);

    when(qualificationMapper.toEntities(toSave)).thenReturn(convertedDtos);
    when(qualificationRepository.save(convertedDtos)).thenReturn(savedEntities);
    when(qualificationMapper.toDTOs(savedEntities)).thenReturn(savedAndConvertedDtos);

    List<QualificationDTO> result = testObj.save(toSave);

    Assert.assertSame(savedAndConvertedDtos, result);
  }

  @Test
  public void saveShouldConvertSingleDtoAndSaveEntity() {
    QualificationDTO unsavedDto = new QualificationDTO(), savedDto = new QualificationDTO();
    ;
    Qualification unsavedEntity1 = new Qualification(), savedEntity2 = new Qualification();

    when(qualificationMapper.toEntity(unsavedDto)).thenReturn(unsavedEntity1);
    when(qualificationRepository.saveAndFlush(unsavedEntity1)).thenReturn(savedEntity2);
    when(qualificationMapper.toDto(savedEntity2)).thenReturn(savedDto);

    QualificationDTO result = testObj.save(unsavedDto);

    Assert.assertSame(savedDto, result);
  }
}