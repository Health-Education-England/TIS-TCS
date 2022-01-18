package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeServiceImplTest {

  public static final long PERSON_ID = 123L;
  @InjectMocks
  private ProgrammeServiceImpl testObj;

  @Mock
  private ProgrammeRepository programmeRepositoryMock;

  @Mock
  private ProgrammeMapper programmeMapperMock;

  @Mock
  private PermissionService permissionServiceMock;

  @Captor
  private ArgumentCaptor<Specification> specificationCaptor;

  @Captor
  private ArgumentCaptor<Pageable> pageableArgumentCaptor;


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
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID))
        .thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes))
        .thenReturn(convertedProgrammes);

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
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID))
        .thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes))
        .thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(convertedProgrammes, result);
  }

  @Test()
  public void findAllShouldDealWithProgrammeObserverRole() {
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

    Assert.assertThat("should return programme dto", result.getContent().get(0),
        CoreMatchers.is(programmeDTO));
  }

  @Test()
  public void advancedSearchShouldDealWithProgrammeObserverRole() {
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
    Assert.assertThat("should return programme dto", result.getContent().get(0),
        CoreMatchers.is(programmeDTO));
  }
}
