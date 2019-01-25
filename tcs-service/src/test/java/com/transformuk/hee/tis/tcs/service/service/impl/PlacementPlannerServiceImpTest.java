package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementPlannerMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlacementPlannerServiceImpTest {

  private static final long SITE_ID = 1L;
  private static final long SPECIALTY_ID = 1L;
  private static final long PROGRAMME_ID = 1L;

  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private SpecialtyRepository specialtyRepositoryMock;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  private PlacementPlannerMapper placementPlannerMapperMock;
  @Mock
  private Specialty specialtyMock;
  @Mock
  private Placement placementMock1, placementMock2;
  @Mock
  private Post postMock;
  @Mock
  private SiteDTO siteDTOMock;
  @Mock
  private SpecialtyDTO specialtyDTOMock;
  @InjectMocks
  private PlacementPlannerServiceImp testObj;


  @Before
  public void setup() {
    when(siteDTOMock.getId()).thenReturn(SITE_ID);
    when(placementMock1.getSiteId()).thenReturn(SITE_ID);
    when(placementMock1.getDateFrom()).thenReturn(LocalDate.now());
    when(placementMock1.getPost()).thenReturn(postMock);
    when(placementMock2.getSiteId()).thenReturn(SITE_ID);
    when(placementMock2.getDateFrom()).thenReturn(LocalDate.now().plusMonths(1));
    when(placementMock2.getPost()).thenReturn(postMock);
  }

  @Test
  public void findPlacementsForProgrammeAndSpecialtyShouldRetrievePlacementsAndTheRestOfTheRequiredData() {

    HashSet<Long> siteIds = Sets.newHashSet(SITE_ID);
    Set<Placement> foundPlacements = Sets.newHashSet(placementMock1, placementMock2);
    List<SiteDTO> foundSites = Lists.newArrayList(siteDTOMock);
    Map<SiteDTO, Set<Post>> siteToPosts = Maps.newHashMap();
    siteToPosts.put(siteDTOMock, Sets.newHashSet(postMock));
    Map<Post, Set<Placement>> postToPlacements = Maps.newHashMap();
    postToPlacements.put(postMock, Sets.newHashSet(placementMock1, placementMock2));
    LocalDate fromDate = LocalDate.now().minusYears(1);
    LocalDate toDate = LocalDate.now().plusYears(1);

    when(specialtyRepositoryMock.findById(SPECIALTY_ID)).thenReturn(Optional.of(specialtyMock));
    when(placementRepositoryMock.findPlacementsByProgrammeIdAndSpecialtyId(PROGRAMME_ID, SPECIALTY_ID, fromDate, toDate)).thenReturn(foundPlacements);
    when(referenceServiceMock.findSitesIdIn(siteIds)).thenReturn(foundSites);
    when(placementPlannerMapperMock.convertSpecialty(eq(specialtyMock), any())).thenReturn(specialtyDTOMock);

    PlacementsResultDTO result = testObj.findPlacementsForProgrammeAndSpecialty(PROGRAMME_ID, SPECIALTY_ID, fromDate, toDate);

    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getSpecialties());
    Assert.assertEquals(1, result.getSpecialties().size());
    Assert.assertEquals(specialtyDTOMock, result.getSpecialties().get(0));

    verify(specialtyRepositoryMock).findById(SPECIALTY_ID);
    verify(placementRepositoryMock).findPlacementsByProgrammeIdAndSpecialtyId(PROGRAMME_ID, SPECIALTY_ID, fromDate, toDate);
    verify(referenceServiceMock).findSitesIdIn(siteIds);
    verify(placementPlannerMapperMock).convertSpecialty(eq(specialtyMock), any());
  }

  @Test
  public void findPlacementForProgrammeAndSpecialtyShouldReturnResultWithEmptyList() {
    when(specialtyRepositoryMock.findById(SPECIALTY_ID)).thenReturn(Optional.empty());

    PlacementsResultDTO result = testObj.findPlacementsForProgrammeAndSpecialty(PROGRAMME_ID, SPECIALTY_ID, null, null);

    Assert.assertNull(result.getSpecialties());

    verifyZeroInteractions(placementRepositoryMock);
    verifyZeroInteractions(referenceServiceMock);
    verifyZeroInteractions(placementPlannerMapperMock);
  }

}