package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProgrammeServiceImplTest {

  public static final long PERSON_ID = 123L;
  @InjectMocks
  private ProgrammeServiceImpl testObj;

  @Mock
  private ProgrammeRepository programmeRepositoryMock;

  @Mock
  private ProgrammeMapper programmeMapperMock;

  @Mock
  private PermissionService permissionServiceMock;

  @Mock
  private ApplicationEventPublisher applicationEventPublisherMock;

  @Captor
  private ArgumentCaptor<Specification<Programme>> specificationCaptor;

  @Captor
  private ArgumentCaptor<Pageable> pageableArgumentCaptor;

  @Captor
  private ArgumentCaptor<ProgrammeCreatedEvent> programmeCreatedEventCaptor;

  @Captor
  private ArgumentCaptor<ProgrammeSavedEvent> programmeSavedEventCaptor;


  @Test
  void findTraineeProgrammesShouldNPEWhenNullIsProvided() {
    assertThrows(NullPointerException.class, () -> testObj.findTraineeProgrammes(null));
    verify(programmeRepositoryMock, never()).findByProgrammeMembershipPersonId(any());
    verify(programmeMapperMock, never()).programmesToProgrammeDTOs(anyList());
  }

  @Test()
  void saveShouldPublishProgrammeCreatedEvent() {
    ProgrammeDTO inputDto = new ProgrammeDTO();
    inputDto.setProgrammeName("Programme A");

    Programme savedProgramme = new Programme();
    savedProgramme.setId(101L);
    savedProgramme.setProgrammeName("Programme A");

    ProgrammeDTO savedDto = new ProgrammeDTO();
    savedDto.setId(101L);
    savedDto.setProgrammeName("Programme A");

    when(programmeMapperMock.programmeDTOToProgramme(inputDto)).thenReturn(savedProgramme);
    when(programmeRepositoryMock.save(savedProgramme)).thenReturn(savedProgramme);
    when(programmeMapperMock.programmeToProgrammeDTO(savedProgramme)).thenReturn(savedDto);

    ProgrammeDTO result = testObj.save(inputDto);

    assertEquals(savedDto, result);
    verify(applicationEventPublisherMock).publishEvent(programmeCreatedEventCaptor.capture());
    assertEquals(savedDto, programmeCreatedEventCaptor.getValue().getProgrammeDTO());
  }

  @Test()
  void updateShouldPublishProgrammeSavedEventWithPreviousAndCurrentDtos() {
    ProgrammeDTO updateDto = new ProgrammeDTO();
    updateDto.setId(11L);
    updateDto.setProgrammeName("Updated Name");

    Programme existingProgramme = new Programme();
    existingProgramme.setId(11L);
    existingProgramme.setProgrammeName("Old Name");

    Programme updatedProgramme = new Programme();
    updatedProgramme.setId(11L);
    updatedProgramme.setProgrammeName("Updated Name");

    ProgrammeDTO previousDto = new ProgrammeDTO();
    previousDto.setId(11L);
    previousDto.setProgrammeName("Old Name");

    ProgrammeDTO currentDto = new ProgrammeDTO();
    currentDto.setId(11L);
    currentDto.setProgrammeName("Updated Name");

    when(programmeRepositoryMock.getOne(11L)).thenReturn(existingProgramme);
    when(programmeMapperMock.programmeToProgrammeDTO(existingProgramme)).thenReturn(previousDto);
    when(programmeMapperMock.programmeDTOToProgramme(updateDto)).thenReturn(updatedProgramme);
    when(programmeRepositoryMock.save(updatedProgramme)).thenReturn(updatedProgramme);
    when(programmeMapperMock.programmeToProgrammeDTO(updatedProgramme)).thenReturn(currentDto);

    ProgrammeDTO result = testObj.update(updateDto);

    assertEquals(currentDto, result);
    verify(applicationEventPublisherMock).publishEvent(programmeSavedEventCaptor.capture());
    ProgrammeSavedEvent event = programmeSavedEventCaptor.getValue();
    assertEquals(previousDto, event.getPreviousProgrammeDto());
    assertEquals(currentDto, event.getProgrammeDTO());
  }

  @Test()
  void saveListShouldPublishProgrammeSavedEventForEachDistinctProgramme() {
    ProgrammeDTO inputDto1 = new ProgrammeDTO();
    inputDto1.setId(1L);
    ProgrammeDTO inputDto2 = new ProgrammeDTO();
    inputDto2.setId(2L);
    List<ProgrammeDTO> inputDtos = Lists.newArrayList(inputDto1, inputDto2);

    Programme existingProgramme1 = new Programme();
    existingProgramme1.setId(1L);
    Programme existingProgramme2 = new Programme();
    existingProgramme2.setId(2L);

    ProgrammeDTO previousDto1 = new ProgrammeDTO();
    previousDto1.setId(1L);
    ProgrammeDTO previousDto2 = new ProgrammeDTO();
    previousDto2.setId(2L);

    Programme programme1 = new Programme();
    programme1.setId(1L);
    Programme programme2 = new Programme();
    programme2.setId(2L);
    List<Programme> programmes = Lists.newArrayList(programme1, programme2);

    ProgrammeDTO updatedDto1 = new ProgrammeDTO();
    updatedDto1.setId(1L);
    ProgrammeDTO updatedDto2 = new ProgrammeDTO();
    updatedDto2.setId(2L);
    List<ProgrammeDTO> updatedDtos = Lists.newArrayList(updatedDto1, updatedDto2);

    when(programmeRepositoryMock.findByIdIn(new HashSet<>(Lists.newArrayList(1L, 2L))))
        .thenReturn(Lists.newArrayList(existingProgramme1, existingProgramme2));
    when(programmeMapperMock.programmeToProgrammeDTO(existingProgramme1)).thenReturn(previousDto1);
    when(programmeMapperMock.programmeToProgrammeDTO(existingProgramme2)).thenReturn(previousDto2);
    when(programmeMapperMock.programmeDTOsToProgrammes(inputDtos)).thenReturn(programmes);
    when(programmeRepositoryMock.saveAll(programmes)).thenReturn(programmes);
    when(programmeMapperMock.programmesToProgrammeDTOs(programmes)).thenReturn(updatedDtos);

    List<ProgrammeDTO> result = testObj.save(inputDtos);

    assertEquals(updatedDtos, result);
    verify(applicationEventPublisherMock, times(2))
        .publishEvent(programmeSavedEventCaptor.capture());

    Map<Long, ProgrammeSavedEvent> eventByProgrammeId = programmeSavedEventCaptor.getAllValues()
        .stream().collect(java.util.stream.Collectors.toMap(
            event -> event.getProgrammeDTO().getId(), event -> event));
    assertEquals(previousDto1, eventByProgrammeId.get(1L).getPreviousProgrammeDto());
    assertEquals(updatedDto1, eventByProgrammeId.get(1L).getProgrammeDTO());
    assertEquals(previousDto2, eventByProgrammeId.get(2L).getPreviousProgrammeDto());
    assertEquals(updatedDto2, eventByProgrammeId.get(2L).getProgrammeDTO());
  }

  @Test()
  void findTraineeProgrammesShouldReturnAnEmptyListWhenNoProgrammesFound() {
    List<Programme> foundProgrammes = Lists.newArrayList();
    List<ProgrammeDTO> convertedProgrammes = Lists.newArrayList();
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID))
        .thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes))
        .thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    assertNotNull(result);
    assertEquals(0, result.size());
    assertEquals(convertedProgrammes, result);
  }

  @Test()
  void findTraineeProgrammesShouldReturnDtosOfProgrammes() {
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
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID))
        .thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes))
        .thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(convertedProgrammes, result);
  }

  @Test()
  void findAllShouldDealWithProgrammeObserverRole() {
    Programme programme = new Programme();
    programme.setId(425L);
    programme.setProgrammeName("BBBBB");

    Page<Programme> foundPage = new PageImpl<>(Lists.newArrayList(programme));

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(425L);
    programmeDTO.setProgrammeName("BBBBB");

    when(permissionServiceMock.isProgrammeObserver()).thenReturn(true);
    Set<Long> programmeIdSet = new HashSet<>();
    programmeIdSet.add(425L);
    when(permissionServiceMock.getUsersProgrammeIds()).thenReturn(programmeIdSet);
    when(programmeRepositoryMock
        .findAll(specificationCaptor.capture(), pageableArgumentCaptor.capture()))
        .thenReturn(foundPage);
    when(programmeMapperMock.programmeToProgrammeDTO(any())).thenReturn(programmeDTO);
    Page<ProgrammeDTO> result = testObj.findAll(null);

    assertEquals(programmeDTO, result.getContent().get(0));
  }

  @Test()
  void advancedSearchShouldDealWithProgrammeObserverRole() {
    Programme programme = new Programme();
    programme.setId(425L);
    programme.setProgrammeName("BBBBB");

    Page<Programme> foundPage = new PageImpl<>(Lists.newArrayList(programme));

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(425L);
    programmeDTO.setProgrammeName("BBBBB");

    when(permissionServiceMock.isProgrammeObserver()).thenReturn(true);
    Set<Long> programmeIdSet = new HashSet<>();
    programmeIdSet.add(425L);
    when(permissionServiceMock.getUsersProgrammeIds()).thenReturn(programmeIdSet);
    when(programmeRepositoryMock
        .findAll(specificationCaptor.capture(), pageableArgumentCaptor.capture()))
        .thenReturn(foundPage);
    when(programmeMapperMock.programmeToProgrammeDTO(any())).thenReturn(programmeDTO);
    Page<ProgrammeDTO> result = testObj.advancedSearch("", null, null);
    assertEquals(programmeDTO, result.getContent().get(0));
  }
}
