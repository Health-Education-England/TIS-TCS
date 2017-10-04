package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  @Before
  public void setup() {
    specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setSpecialtyGroup(new SpecialtyGroupDTO());
    specialtyDTO.setIntrepidId(INTREPID_ID);
    specialtyDTO.setSpecialtyTypes(Sets.newHashSet(SUB_SPECIALTY));
    specialtyDTO.setNhsSpecialtyCode(NHS_CODE);
    specialtyDTO.setCollege(COLLEGE);
    specialtyDTO.setStatus(Status.INACTIVE);
  }

  @Test
  public void saveShouldSaveSpecialty() {
    SpecialtyDTO expectedSpecialtyDTO = new SpecialtyDTO();
    expectedSpecialtyDTO.setId(12345L);
    when(specialtyMapperMock.specialtyDTOToSpecialty(specialtyDTO)).thenReturn(specialtyMock);
    when(specialtyRepositoryMock.save(specialtyMock)).thenReturn(specialtyMock);
    when(specialtyMapperMock.specialtyToSpecialtyDTO(specialtyMock)).thenReturn(expectedSpecialtyDTO);

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
    when(specialtyMapperMock.specialtyToSpecialtyDTO(specialtyMock)).thenReturn(expectedSpecialtyDTO);

    SpecialtyDTO result = testObj.save(specialtyDTO);

    Assert.assertEquals(expectedSpecialtyDTO, result);
    verify(specialtyMapperMock).specialtyDTOToSpecialty(specialtyDTO);
    verify(specialtyMock).setStatus(statusCaptor.capture());

    Status capturedStatus = statusCaptor.getValue();
    Assert.assertEquals(Status.CURRENT, capturedStatus);

    verify(specialtyRepositoryMock).save(specialtyMock);
    verify(specialtyMapperMock).specialtyToSpecialtyDTO(specialtyMock);
  }
}