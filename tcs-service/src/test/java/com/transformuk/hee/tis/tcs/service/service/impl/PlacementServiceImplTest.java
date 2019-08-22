package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementSiteType;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementSite;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSpecialtyMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PlacementServiceImplTest {

  private static final long PLACEMENT_ID = 1L;
  private static final Long number = 1L;
  @InjectMocks
  private PlacementServiceImpl testObj;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private PlacementDetailsRepository placementDetailsRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private PlacementSupervisorRepository placementSupervisorRepositoryMock;
  @Mock
  private PlacementMapper placementMapperMock;
  @Mock
  private PlacementDetailsMapper placementDetailsMapperMock;
  @Mock
  private PlacementSiteMapper placementSiteMapper;
  @Mock
  private PlacementSpecialtyMapper placementSpecialtyMapperMock;
  @Mock
  private Placement placementMock;
  @Mock
  private PlacementDTO placementDTOMock;
  @Mock
  private SqlQuerySupplier sqlQuerySupplierMock;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;
  @Mock
  private PostRepository postRepositoryMock;
  @Mock
  private Clock clock;
  @Captor
  private ArgumentCaptor<LocalDate> toDateCaptor;
  @Captor
  private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;
  @Captor
  private ArgumentCaptor<PlacementRowMapper> placementRowMapperArgumentCaptor;
  @Captor
  private ArgumentCaptor<Long> longArgumentCaptor;

  public static PlacementSummaryDTO createPlacementSummaryDTO() {
    return new PlacementSummaryDTO(null, null, number,
        "Elbows", number, "In Post", "CURRENT", "Joe", "Bloggs",
        number, number, null, null);
  }

  @Before
  public void setup() {
    when(clock.instant()).thenReturn(Instant.now());
    when(clock.getZone()).thenReturn(ZoneId.systemDefault());
  }

  @Test
  public void closePlacementShouldClosePlacementBySettingToDate() {
    when(placementRepositoryMock.findById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    doNothing().when(placementMock).setDateTo(toDateCaptor.capture());
    when(placementRepositoryMock.saveAndFlush(placementMock)).thenReturn(placementMock);
    when(placementMapperMock.placementToPlacementDTO(eq(placementMock), anyMap()))
        .thenReturn(placementDTOMock);

    PlacementDTO result = testObj.closePlacement(PLACEMENT_ID);

    Assert.assertEquals(placementDTOMock, result);

    LocalDate toDateCapture = toDateCaptor.getValue();
    Assert.assertEquals(LocalDate.now().minusDays(1), toDateCapture);
  }

  @Test
  public void shouldReturnPlacementsForATraineeInOrder() throws Exception {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
    Date latest_date = simpleDateFormat.parse("2090/12/01");
    Date second_latest_date = simpleDateFormat.parse("2020/12/01");
    Date earliest_date = simpleDateFormat.parse("2010/12/01");
    Date bulk_date = simpleDateFormat.parse("2018/12/01");
    Long traineeId = 1L;
    Long id1 = 1L;
    Long id2 = 2L;
    Long id3 = 3L;
    Long id4 = 4L;
    Long id5 = 5L;
    BigDecimal wte = new BigDecimal(0.6);

    PlacementSummaryDTO placement_latest = createPlacementSummaryDTO(), placement_second_latest = createPlacementSummaryDTO(),
        placement_earliest = createPlacementSummaryDTO(), placement_null = createPlacementSummaryDTO(),
        placement_null_2 = createPlacementSummaryDTO();

    placement_latest.setDateTo(latest_date);
    placement_latest.setPlacementId(id1);
    placement_latest.setPlacementWholeTimeEquivalent(wte);;
    placement_second_latest.setDateTo(second_latest_date);
    placement_second_latest.setPlacementId(id2);
    placement_earliest.setDateTo(earliest_date);
    placement_earliest.setPlacementId(id3);
    placement_null.setDateTo(null);
    placement_null.setPlacementId(id4);
    placement_null_2.setDateTo(null);
    placement_null_2.setPlacementId(id5);

    List<PlacementSummaryDTO> placements = Lists
        .newArrayList(placement_second_latest, placement_earliest, placement_null,
            placement_latest, placement_null_2);

    for (int i = 6; i < 2000; i++) {
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId((long) i);

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE traineeId = :traineeId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY))
        .thenReturn(sqlQueryMock);
    when(namedParameterJdbcTemplateMock.query(eq(sqlQueryMock), mapArgumentCaptor.capture(),
        placementRowMapperArgumentCaptor.capture())).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForTrainee(traineeId, "Dr in Training");

    int sizeOfResult = result.size();
    Assert.assertEquals(wte, result.get(0).getPlacementWholeTimeEquivalent());
    Assert.assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult - 2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult - 1).getDateTo());

    Map<String, Object> capturedParams = mapArgumentCaptor.getValue();
    Assert.assertTrue(capturedParams.containsKey("traineeId"));

    PlacementRowMapper capturedRowMapper = placementRowMapperArgumentCaptor.getValue();
    Assert.assertNotNull(capturedRowMapper);
  }

  @Test
  public void shouldReturnPlacementsForAPostInOrder() throws Exception {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
    Date latest_date = simpleDateFormat.parse("2090/12/01");
    Date second_latest_date = simpleDateFormat.parse("2020/12/01");
    Date earliest_date = simpleDateFormat.parse("2010/12/01");
    Date bulk_date = simpleDateFormat.parse("2018/12/01");
    Long postId = 1L;
    Long id1 = 1L;
    Long id2 = 2L;
    Long id3 = 3L;
    Long id4 = 4L;
    Long id5 = 5L;

    PlacementSummaryDTO placement_latest = createPlacementSummaryDTO(), placement_second_latest = createPlacementSummaryDTO(),
        placement_earliest = createPlacementSummaryDTO(), placement_null = createPlacementSummaryDTO(),
        placement_null_2 = createPlacementSummaryDTO();

    placement_latest.setDateTo(latest_date);
    placement_latest.setPlacementId(id1);
    placement_second_latest.setDateTo(second_latest_date);
    placement_second_latest.setPlacementId(id2);
    placement_earliest.setDateTo(earliest_date);
    placement_earliest.setPlacementId(id3);
    placement_null.setDateTo(null);
    placement_null.setPlacementId(id4);
    placement_null_2.setDateTo(null);
    placement_null_2.setPlacementId(id5);

    List<PlacementSummaryDTO> placements = Lists
        .newArrayList(placement_second_latest, placement_earliest, placement_null,
            placement_latest, placement_null_2);

    for (int i = 6; i < 1000; i++) {
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId((long) i);

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE p.postId = :postId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY))
        .thenReturn(sqlQueryMock);
    when(namedParameterJdbcTemplateMock.query(eq(sqlQueryMock), mapArgumentCaptor.capture(),
        placementRowMapperArgumentCaptor.capture())).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForPost(postId);

    int sizeOfResult = result.size();
    Assert.assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult - 2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult - 1).getDateTo());

    Map<String, Object> capturedParams = mapArgumentCaptor.getValue();
    Assert.assertTrue(capturedParams.containsKey("postId"));

    PlacementRowMapper capturedRowMapper = placementRowMapperArgumentCaptor.getValue();
    Assert.assertNotNull(capturedRowMapper);
  }

  @Test
  public void placementForPostShouldLimitPostsIfMoreThan1k() {
    long postId = 1L;
    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE p.postId = :postId";
    List<PlacementSummaryDTO> queryResult = Lists.newArrayList();
    for (int i = 0; i < 5000; i++) {
      PlacementSummaryDTO placementSummaryDTO = new PlacementSummaryDTO();
      queryResult.add(placementSummaryDTO);
    }

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY))
        .thenReturn(sqlQueryMock);
    when(namedParameterJdbcTemplateMock.query(eq(sqlQueryMock), mapArgumentCaptor.capture(),
        placementRowMapperArgumentCaptor.capture())).thenReturn(queryResult);

    List<PlacementSummaryDTO> result = testObj.getPlacementForPost(postId);

    Assert.assertTrue(result.size() <= 1000);
  }

  @Test
  public void isEligibleForChangedDatesNotificationShouldReturnTrueWhenUpdatedPlacementIsEligibleForNotification() {
    LocalDate dateFiveMonthsAgo = LocalDate.now().minusMonths(5);
    LocalDate dateOneMonthsAgo = LocalDate.now().minusMonths(1);
    Long existingPlacementId = 1L;

    Placement currentPlacement = new Placement();
    currentPlacement.setId(existingPlacementId);
    currentPlacement.setDateFrom(dateFiveMonthsAgo);

    PlacementDetailsDTO updatedPlacementDetails = new PlacementDetailsDTO();
    updatedPlacementDetails.setDateFrom(dateOneMonthsAgo);

    Post foundPostMock = mock(Post.class);

    when(postRepositoryMock.findPostByPlacementHistoryId(longArgumentCaptor.capture()))
        .thenReturn(Optional.of(foundPostMock));

    boolean result = testObj
        .isEligibleForChangedDatesNotification(updatedPlacementDetails, currentPlacement);

    Assert.assertTrue(result);

    Long capturedPlacementId = longArgumentCaptor.getValue();
    Assert.assertEquals(existingPlacementId, capturedPlacementId);
  }

  @Test
  public void isEligibleForChangedDatesNotificationShouldReturnFalseWhenCurrentAndUpdatedPlacementFromDatesAreTheSame() {
    LocalDate dateFiveMonthsAgo = LocalDate.now().minusMonths(5);
    Long existingPlacementId = 1L;

    Placement currentPlacement = new Placement();
    currentPlacement.setId(existingPlacementId);
    currentPlacement.setDateFrom(dateFiveMonthsAgo);

    PlacementDetailsDTO updatedPlacementDetails = new PlacementDetailsDTO();
    updatedPlacementDetails.setDateFrom(dateFiveMonthsAgo);

    boolean result = testObj
        .isEligibleForChangedDatesNotification(updatedPlacementDetails, currentPlacement);

    Assert.assertFalse(result);

    verifyZeroInteractions(postRepositoryMock);
  }

  /**
   * Test that the placement's addedDate is populated and the amendedDate is not populated when the
   * placement ID is null.
   */
  @Test
  public void testCreateDetails_placementIdNull_addedDatePopulatedAmendedDateNotPopulated() {
    // Set up test data.
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    PlacementDetails placementDetails = new PlacementDetails();
    PlacementDetails savedPlacementDetails = new PlacementDetails();
    savedPlacementDetails.setId(1L);

    // Record expectations.
    when(placementDetailsMapperMock.placementDetailsDTOToPlacementDetails(placementDetailsDto))
        .thenReturn(placementDetails);
    when(placementDetailsRepositoryMock.saveAndFlush(placementDetails))
        .thenReturn(savedPlacementDetails);
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(savedPlacementDetails))
        .thenReturn(new PlacementDetailsDTO());
    when(placementSpecialtyMapperMock.toDTOs(any())).thenReturn(Collections.emptySet());
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1L);
    when(placementSupervisorRepositoryMock.saveAll(any())).thenReturn(null);

    // Call the method under test.
    testObj.createDetails(placementDetailsDto);

    // Perform assertions.
    Assert.assertThat("The placement's added date did not match the expected value.",
        placementDetails.getAddedDate(), CoreMatchers.is(LocalDateTime.now(clock)));
    Assert.assertThat("The placement's amended date did not match the expected value.",
        placementDetails.getAmendedDate(), CoreMatchers.nullValue());
  }

  /**
   * Test that the placement's addedDate is not populated and the amendedDate is populated when the
   * placement ID is not null.
   */
  @Test
  public void testCreateDetails_placementIdNotNull_addedDateNotPopulatedAmendedDatePopulated() {
    // Set up test data.
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(1L);
    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    // Record expectations.
    when(placementDetailsMapperMock.placementDetailsDTOToPlacementDetails(placementDetailsDto))
        .thenReturn(placementDetails);
    when(placementDetailsRepositoryMock.saveAndFlush(placementDetails))
        .thenReturn(placementDetails);
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(placementDetails))
        .thenReturn(new PlacementDetailsDTO());
    when(placementSpecialtyMapperMock.toDTOs(any())).thenReturn(Collections.emptySet());
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1L);
    when(placementSupervisorRepositoryMock.saveAll(any())).thenReturn(null);

    // Call the method under test.
    testObj.createDetails(placementDetailsDto);

    // Perform assertions.
    Assert.assertThat("The placement's added date did not match the expected value.",
        placementDetails.getAddedDate(), CoreMatchers.nullValue());
    Assert.assertThat("The placement's amended date did not match the expected value.",
        placementDetails.getAmendedDate(), CoreMatchers.is(LocalDateTime.now(clock)));
  }

  /**
   * Test that no sites are contained in the DTO when no sites are given.
   */
  @Test
  public void testCreateDetails_noSites_noSites() {
    // Set up test data.
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    // Record expectations.
    when(placementDetailsMapperMock.placementDetailsDTOToPlacementDetails(placementDetailsDto))
        .thenReturn(placementDetails);
    when(placementDetailsRepositoryMock.saveAndFlush(placementDetails))
        .thenReturn(placementDetails);
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(placementDetails))
        .thenReturn(new PlacementDetailsDTO());
    when(placementSpecialtyMapperMock.toDTOs(any())).thenReturn(Collections.emptySet());
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1L);
    when(placementSupervisorRepositoryMock.saveAll(any())).thenReturn(null);

    // Call the method under test.
    PlacementDetailsDTO updatedPlacementDetailsDto = testObj.createDetails(placementDetailsDto);

    // Perform assertions.
    Set<PlacementSiteDTO> sites = updatedPlacementDetailsDto.getSites();
    Assert
        .assertThat("The placement's number of sites did not match the expected value.",
            sites.size(),
            CoreMatchers.is(0));
  }

  /**
   * Test that sites are contained in the DTO when sites are given.
   */
  @Test
  public void testCreateDetails_hasSites_hasSites() {
    // Set up test data.
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();

    PlacementSiteDTO placementSiteDto1 = new PlacementSiteDTO();
    placementSiteDto1.setSiteId(1L);
    placementSiteDto1.setPlacementSiteType(PlacementSiteType.OTHER);
    PlacementSiteDTO placementSiteDto2 = new PlacementSiteDTO();
    placementSiteDto2.setSiteId(2L);
    placementSiteDto2.setPlacementSiteType(PlacementSiteType.OTHER);
    Set<PlacementSiteDTO> siteDtos = Sets.newHashSet(placementSiteDto1, placementSiteDto2);
    placementDetailsDto.setSites(siteDtos);

    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    PlacementSite placementSite1 = new PlacementSite();
    placementSite1.setId(1L);
    placementSite1.setPlacementSiteType(PlacementSiteType.OTHER);
    placementSite1.setPlacement(placementDetails);
    PlacementSite placementSite2 = new PlacementSite();
    placementSite2.setId(2L);
    placementSite2.setPlacementSiteType(PlacementSiteType.OTHER);
    placementSite2.setPlacement(placementDetails);
    Set<PlacementSite> placementSites = Sets.newHashSet(placementSite1, placementSite2);

    PlacementDetails updatedPlacementDetails = new PlacementDetails();
    updatedPlacementDetails.setId(1L);
    updatedPlacementDetails.setSites(placementSites);
    updatedPlacementDetails.setAmendedDate(LocalDateTime.now(clock));

    // Record expectations.
    when(placementDetailsMapperMock.placementDetailsDTOToPlacementDetails(placementDetailsDto))
        .thenReturn(placementDetails);
    when(placementSiteMapper.toEntity(placementSiteDto1)).thenReturn(placementSite1);
    when(placementSiteMapper.toEntity(placementSiteDto2)).thenReturn(placementSite2);
    when(placementDetailsRepositoryMock.saveAndFlush(eq(updatedPlacementDetails)))
        .thenReturn(updatedPlacementDetails);
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(placementDetails))
        .thenReturn(new PlacementDetailsDTO());
    when(placementSpecialtyMapperMock.toDTOs(any())).thenReturn(Collections.emptySet());
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1L);
    when(placementSupervisorRepositoryMock.saveAll(any())).thenReturn(null);
    when(placementSiteMapper.toDto(placementSite1)).thenReturn(placementSiteDto1);
    when(placementSiteMapper.toDto(placementSite2)).thenReturn(placementSiteDto2);

    // Call the method under test.
    PlacementDetailsDTO updatedPlacementDetailsDto = testObj.createDetails(placementDetailsDto);

    // Perform assertions.
    Set<PlacementSiteDTO> sites = updatedPlacementDetailsDto.getSites();
    Assert
        .assertThat("The number of placement sites did not match the expected value.", sites.size(),
            CoreMatchers.is(2));

    for (PlacementSiteDTO site : sites) {
      Assert.assertThat("The placement site's type did not match the expected value.",
          site.getPlacementSiteType(), CoreMatchers.is(PlacementSiteType.OTHER));
      Assert.assertThat("The placement site's placement ID did not match the expected value.",
          site.getPlacementId(), CoreMatchers.is(1L));
    }
  }
}
