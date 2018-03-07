package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
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
    Assert.assertEquals(LocalDate.now(), toDateCapture);

  }

}