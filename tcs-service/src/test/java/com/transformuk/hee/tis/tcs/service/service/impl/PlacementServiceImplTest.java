package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlacementServiceImplTest {

  public static final long PLACEMENT_ID = 1L;
  @InjectMocks
  private PlacementServiceImpl testObj;

  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private PlacementMapper placementMapperMock;
  @Mock
  private Placement placementMock;
  @Mock
  private PlacementDTO placementDTOMock;
  @Captor
  private ArgumentCaptor<LocalDate> toDateCaptor;

  @Before
  public void setup() {

  }

  @Test
  public void closePlacementShouldClosePlacementBySettingToDate() {
    when(placementRepositoryMock.findOne(PLACEMENT_ID)).thenReturn(placementMock);
    doNothing().when(placementMock).setDateTo(toDateCaptor.capture());
    when(placementRepositoryMock.saveAndFlush(placementMock)).thenReturn(placementMock);
    when(placementMapperMock.placementToPlacementDTO(placementMock)).thenReturn(placementDTOMock);

    PlacementDTO result = testObj.closePlacement(PLACEMENT_ID);

    Assert.assertEquals(placementDTOMock, result);

    LocalDate toDateCapture = toDateCaptor.getValue();
    Assert.assertEquals(LocalDate.now().minusDays(1), toDateCapture);

  }
  /*@Test()
  public void placementsShouldBeOrderedWithDateNullValues() {
    PlacementSummaryDTO placement1 = new PlacementSummaryDTO();
    placement1.setDateTo(null);
    PlacementSummaryDTO placement2 = new PlacementSummaryDTO();
    placement2.setDateTo(new Date("18-07-2018"));
    PlacementSummaryDTO placement3 = new PlacementSummaryDTO();
    placement3.setDateTo(new Date("10-07-2017"));

    List<PlacementSummaryDTO> inputPlacements = Lists.newArrayList(placement1, placement2, placement3);
    testObj.getPlacementForTrainee()

    List<Programme> foundProgrammes = Lists.newArrayList(programme1, programme2);
    List<ProgrammeDTO> convertedProgrammes = Lists.newArrayList(programmeDTO1, programmeDTO2);
    when(programmeRepositoryMock.findByProgrammeMembershipPersonId(PERSON_ID)).thenReturn(foundProgrammes);
    when(programmeMapperMock.programmesToProgrammeDTOs(foundProgrammes)).thenReturn(convertedProgrammes);

    List<ProgrammeDTO> result = testObj.findTraineeProgrammes(PERSON_ID);

    verify(programmeRepositoryMock).findByProgrammeMembershipPersonId(PERSON_ID);
    verify(programmeMapperMock).programmesToProgrammeDTOs(foundProgrammes);

    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.size());
    Assert.assertEquals(convertedProgrammes, result);
  }*/

}