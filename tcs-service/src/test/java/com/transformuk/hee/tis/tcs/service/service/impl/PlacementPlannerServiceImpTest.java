package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementPlannerMapper;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlacementPlannerServiceImpTest {

  private static final long SITE_ID = 1L;
  private static final long SPECIALTY_ID = 1L;
  private static final long PROGRAMME_ID = 1L;
  private static final LocalDate DATE_FROM_1 = LocalDate.now().minusMonths(6);
  private static final LocalDate DATE_TO_1 = LocalDate.now().minusMonths(3);
  private static final LocalDate DATE_FROM_2 = LocalDate.now().minusMonths(12);
  private static final LocalDate DATE_TO_2 = LocalDate.now().minusMonths(9);
  private static final Long POST1_ID = 1L;
  private static final Long POST2_ID = 2L;

  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private PostRepository postRepositoryMock;
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
  private Post postMock1, postMock2;
  @Mock
  private PostSite postSiteMock;
  @Mock
  private SiteDTO siteDTOMock;
  @Mock
  private SpecialtyDTO specialtyDTOMock;
  @InjectMocks
  private PlacementPlannerServiceImp testObj;
  private Set<PostSite> postSites;
  @Captor
  private ArgumentCaptor<Map<SiteDTO, Map<Post, List<Placement>>>> siteMapCaptor;


  @Before
  public void setup() {
    when(siteDTOMock.getId()).thenReturn(SITE_ID);
    when(placementMock1.getSiteId()).thenReturn(SITE_ID);
    when(placementMock1.getPost()).thenReturn(postMock1);
    when(placementMock1.getDateFrom()).thenReturn(DATE_FROM_1);
    when(placementMock1.getDateTo()).thenReturn(DATE_TO_1);
    when(placementMock2.getSiteId()).thenReturn(SITE_ID);
    when(placementMock2.getPost()).thenReturn(postMock1);
    when(placementMock2.getDateFrom()).thenReturn(DATE_FROM_2);
    when(placementMock2.getDateTo()).thenReturn(DATE_TO_2);
    when(postMock1.getPlacementHistory()).thenReturn(new HashSet<>());
    when(postMock2.getPlacementHistory()).thenReturn(new HashSet<>());
    postSites = Sets.newHashSet();
    postSites.add(postSiteMock);
    when(postMock1.getSites()).thenReturn(postSites);
    when(postMock2.getSites()).thenReturn(postSites);
    when(postMock1.getId()).thenReturn(POST1_ID);
    when(postMock2.getId()).thenReturn(POST2_ID);
    when(postSiteMock.getPostSiteType()).thenReturn(PostSiteType.PRIMARY);
    when(postSiteMock.getSiteId()).thenReturn(SITE_ID);
  }

  @Test
  public void findPlacementsForProgrammeAndSpecialtyShouldRetrievePlacementsAndTheRestOfTheRequiredData() {

    HashSet<Long> siteIds = Sets.newHashSet(SITE_ID);
    Set<Placement> foundPlacements = Sets.newHashSet(placementMock1, placementMock2);
    Set<Post> foundPosts = Sets.newHashSet(postMock1, postMock2); // postMock2 has no placements
    List<SiteDTO> foundSites = Lists.newArrayList(siteDTOMock);
    Map<SiteDTO, Set<Post>> siteToPosts = Maps.newHashMap();
    siteToPosts.put(siteDTOMock, Sets.newHashSet(postMock1));
    Map<Post, Set<Placement>> postToPlacements = Maps.newHashMap();
    postToPlacements.put(postMock1, Sets.newHashSet(placementMock1, placementMock2));
    LocalDate fromDate = LocalDate.now().minusYears(2);
    LocalDate toDate = LocalDate.now().plusYears(2);
    Set<Long> postIds = new HashSet<>();
    postIds.add(postMock1.getId());
    postIds.add(postMock2.getId());

    when(specialtyRepositoryMock.findById(SPECIALTY_ID)).thenReturn(Optional.of(specialtyMock));
    when(postRepositoryMock.findPostsByProgrammeIdAndSpecialtyId(PROGRAMME_ID, SPECIALTY_ID))
        .thenReturn(foundPosts);
    when(placementRepositoryMock.findPlacementsByPostIds(postIds)).thenReturn(foundPlacements);
    when(referenceServiceMock.findSitesIdIn(siteIds)).thenReturn(foundSites);
    when(placementPlannerMapperMock.convertSpecialty(eq(specialtyMock), siteMapCaptor.capture()))
        .thenReturn(specialtyDTOMock);

    PlacementsResultDTO result = testObj
        .findPlacementsForProgrammeAndSpecialty(PROGRAMME_ID, SPECIALTY_ID, fromDate, toDate);

    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getSpecialties());
    Assert.assertEquals(1, result.getSpecialties().size());
    Assert.assertEquals(specialtyDTOMock, result.getSpecialties().get(0));
    Map<SiteDTO, Map<Post, List<Placement>>> siteDTOMap = siteMapCaptor.getValue();
    Map<Post, List<Placement>> postMap = siteDTOMap.get(siteDTOMock);
    Assert.assertEquals(2, postMap.size());
    Assert.assertTrue(postMap.containsKey(postMock1));
    Assert.assertTrue(postMap.containsKey(postMock2));
    Assert.assertEquals(2, postMap.get(postMock1).size());
    Assert.assertEquals(0, postMap.get(postMock2).size());

    verify(specialtyRepositoryMock).findById(SPECIALTY_ID);
    verify(placementRepositoryMock).findPlacementsByPostIds(postIds);
    verify(referenceServiceMock).findSitesIdIn(siteIds);
    verify(placementPlannerMapperMock).convertSpecialty(eq(specialtyMock), any());
  }

  @Test
  public void findPlacementForProgrammeAndSpecialtyShouldReturnResultWithEmptyList() {
    when(specialtyRepositoryMock.findById(SPECIALTY_ID)).thenReturn(Optional.empty());

    PlacementsResultDTO result = testObj
        .findPlacementsForProgrammeAndSpecialty(PROGRAMME_ID, SPECIALTY_ID, null, null);

    Assert.assertNull(result.getSpecialties());

    verifyZeroInteractions(placementRepositoryMock);
    verifyZeroInteractions(referenceServiceMock);
    verifyZeroInteractions(placementPlannerMapperMock);
  }

}
