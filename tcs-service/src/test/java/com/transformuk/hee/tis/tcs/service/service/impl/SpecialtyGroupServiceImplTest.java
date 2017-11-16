package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyGroupMapper;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

;

@RunWith(MockitoJUnitRunner.class)
public class SpecialtyGroupServiceImplTest {


  private static final String INTREPID_ID = "12345";
  private static final String NHS_CODE = "NHS_CODE";
  private static final String COLLEGE = "A MEDIA COLLEGE";
  private static final String SPECIALTY_GROUP_NAME = "AAAAAAAAAA";
  private static final String SPECIALTY_NAME_1 = "BBBBBBBBBB";
  private static final String SPECIALTY_NAME_2 = "CCCCCCCCCC";
  private static final SpecialtyType SUB_SPECIALTY = SpecialtyType.SUB_SPECIALTY;

  @InjectMocks
  private SpecialtyGroupServiceImpl specialtyGroupServiceImpl;

  @Mock
  private SpecialtyGroupRepository specialtyGroupRepositoryMock;

  @Mock
  private SpecialtyRepository specialtyRepository;

  @Mock
  private SpecialtyGroupMapper specialtyGroupMapperMock;

  @Mock
  private Specialty specialtyMock1;

  @Mock
  private  Specialty specialtyMock2;

  @Mock
  private SpecialtyGroup specialtyGroupMock;

  @Mock
  private SpecialtyDTO specialtyDTO1;

  @Mock
  private SpecialtyDTO specialtyDTO2;

  @Mock
  private SpecialtyGroupDTO specialtyGroupDTO;

  @Before
  public void setup() {
    specialtyDTO1 = new SpecialtyDTO();
    specialtyDTO1.setName(SPECIALTY_NAME_1);
    specialtyDTO1.setId(1111L);
    specialtyDTO1.setSpecialtyCode(NHS_CODE);

    specialtyMock1 = new Specialty();
    specialtyMock1.setName(SPECIALTY_NAME_1);
    specialtyMock1.setId(1212L);
    specialtyMock1.setSpecialtyCode(NHS_CODE);

    specialtyDTO2 = new SpecialtyDTO();
    specialtyDTO2.setName(SPECIALTY_NAME_2);
    specialtyDTO2.setId(2222L);
    specialtyDTO2.setSpecialtyCode(NHS_CODE);

    specialtyMock2 = new Specialty();
    specialtyMock2.setName(SPECIALTY_NAME_2);
    specialtyMock2.setId(2121L);
    specialtyMock2.setSpecialtyCode(NHS_CODE);

    specialtyGroupDTO = new SpecialtyGroupDTO();
    specialtyGroupDTO.setName(SPECIALTY_GROUP_NAME);
    specialtyGroupDTO.setId(1234L);

    specialtyGroupMock = new SpecialtyGroup();
    specialtyGroupMock.setName(SPECIALTY_GROUP_NAME);
    specialtyGroupMock.setId(4321L);

    Set<SpecialtyDTO> specialtyDTOS = Sets.newLinkedHashSet(specialtyDTO1, specialtyDTO2);
    specialtyGroupDTO.setSpecialties(specialtyDTOS);

    Set<Specialty> specialties = Sets.newLinkedHashSet(specialtyMock1, specialtyMock2);
    specialtyGroupMock.setSpecialties(specialties);
  }

  @Test
  public void shouldSaveSpecialtyGroup() {
    SpecialtyGroupDTO expectedSpecialtyGroupDTO = specialtyGroupDTO;

    when(specialtyGroupMapperMock.specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO)).thenReturn(specialtyGroupMock);
    when(specialtyGroupRepositoryMock.save(specialtyGroupMock)).thenReturn(specialtyGroupMock);
    when(specialtyGroupMapperMock.specialtyGroupToSpecialtyGroupDTO(specialtyGroupMock)).thenReturn(expectedSpecialtyGroupDTO);

    SpecialtyGroupDTO result = specialtyGroupServiceImpl.save(expectedSpecialtyGroupDTO);

    Assert.assertEquals(expectedSpecialtyGroupDTO, result);
    verify(specialtyGroupMapperMock).specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO);
    verify(specialtyGroupRepositoryMock).save(specialtyGroupMock);
    verify(specialtyGroupMapperMock).specialtyGroupToSpecialtyGroupDTO(specialtyGroupMock);
  }

  @Test
  public void shouldUpdateSpecialtiesInSpecialtyGroupAddSpecialty() {
    SpecialtyGroupDTO expectedSpecialtyGroupDTO = specialtyGroupDTO;

    when(specialtyGroupMapperMock.specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO)).thenReturn(specialtyGroupMock);
    when(specialtyGroupRepositoryMock.save(specialtyGroupMock)).thenReturn(specialtyGroupMock);
    when(specialtyGroupMapperMock.specialtyGroupToSpecialtyGroupDTO(specialtyGroupMock)).thenReturn(expectedSpecialtyGroupDTO);

    // Save into repository
    SpecialtyGroupDTO result = specialtyGroupServiceImpl.save(expectedSpecialtyGroupDTO);

    // Update the specialties
    SpecialtyDTO specialtyDTOnew = new SpecialtyDTO();
    specialtyDTOnew.setId(9876L);
    specialtyDTOnew.setName("NEW SPECIALTY");

    Set<SpecialtyDTO> specialtyDTOS = Sets.newLinkedHashSet(specialtyDTO1, specialtyDTO2, specialtyDTOnew);
    expectedSpecialtyGroupDTO.setSpecialties(specialtyDTOS);

    // Update in repository
    SpecialtyGroupDTO updatedResult = specialtyGroupServiceImpl.save(expectedSpecialtyGroupDTO);
    Assert.assertEquals(expectedSpecialtyGroupDTO, updatedResult);
    Assert.assertEquals(updatedResult.getSpecialties(), specialtyDTOS);
  }

  @Test
  public void shouldUpdateSpecialtiesInSpecialtyGroupDeleteSpecialty() {
    SpecialtyGroupDTO expectedSpecialtyGroupDTO = specialtyGroupDTO;
    when(specialtyGroupMapperMock.specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO)).thenReturn(specialtyGroupMock);
    when(specialtyGroupRepositoryMock.save(specialtyGroupMock)).thenReturn(specialtyGroupMock);
    when(specialtyGroupMapperMock.specialtyGroupToSpecialtyGroupDTO(specialtyGroupMock)).thenReturn(expectedSpecialtyGroupDTO);

    // Save into repository
    SpecialtyGroupDTO result = specialtyGroupServiceImpl.save(expectedSpecialtyGroupDTO);

    // Remove a specialty
    Set<SpecialtyDTO> specialtyDTOS = Sets.newLinkedHashSet(specialtyDTO1);
    expectedSpecialtyGroupDTO.setSpecialties(specialtyDTOS);

    // Update in repository
    SpecialtyGroupDTO updatedResult = specialtyGroupServiceImpl.save(expectedSpecialtyGroupDTO);
    Assert.assertEquals(expectedSpecialtyGroupDTO, updatedResult);
    Assert.assertEquals(updatedResult.getSpecialties(), specialtyDTOS);
  }
}
