package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState.APPROVED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PlacementLog;
import com.transformuk.hee.tis.tcs.service.model.PlacementSite;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementEsrEventRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementEsrEventDtoMapper;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PlacementServiceImplTest {

  private static final Long PLACEMENT_ID = 1L, PLACEMENT2_ID = 2L;
  private static final Long number = 1L;
  public static final Long POSITION_NUMBER = 1111L;
  public static final Long POSITION_ID = 2222L;
  public static final String ESR_FILENAME_TXT = "esr_filename.txt";
  @Spy
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
  @Mock
  private ProgrammeRepository programmeRepository;
  @Mock
  private PlacementLogServiceImpl placementLogServiceImplMock;
  @Mock
  private PlacementDetailsDecorator placementDetailsDecorator;
  @Mock
  private PlacementEsrEventRepository placementEsrEventRepositoryMock;
  @Mock
  private PlacementEsrEventDtoMapper placementEsrExportedDtoMapper;
  @Captor
  private ArgumentCaptor<LocalDate> toDateCaptor;
  @Captor
  private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;
  @Captor
  private ArgumentCaptor<PlacementRowMapper> placementRowMapperArgumentCaptor;
  @Captor
  private ArgumentCaptor<Long> longArgumentCaptor;
  @Captor
  private ArgumentCaptor<PlacementEsrEvent> placementEsrEventArgumentCaptor;

  public static PlacementSummaryDTO createPlacementSummaryDTO() {
    return new PlacementSummaryDTO(null, null, number, number,
        "Elbows", number, "In Post", "CURRENT", "Joe", "Bloggs", "Joe", "Bloggs",
        number, "emailId", "F1", number, null, null, null);
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
    placement_latest.setPlacementWholeTimeEquivalent(wte);
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
  public void populateEsrEventsShouldFindEventsForPlacementDetails() {
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(PLACEMENT_ID);

    PlacementEsrEvent event1Mock = mock(PlacementEsrEvent.class);
    PlacementEsrEvent event2Mock = mock(PlacementEsrEvent.class);
    Set<PlacementEsrEvent> foundEvents = Sets.newHashSet(event1Mock, event2Mock);
    when(placementEsrEventRepositoryMock
        .findPlacementEsrEventByPlacementIdIn(Collections.singletonList(PLACEMENT_ID)))
        .thenReturn(foundEvents);

    PlacementEsrEventDto placementEsrEventDto1 = mock(PlacementEsrEventDto.class);
    PlacementEsrEventDto placementEsrEventDto2 = mock(PlacementEsrEventDto.class);
    Set<PlacementEsrEventDto> placementEsrEventDtos = new HashSet<>();
    placementEsrEventDtos.add(placementEsrEventDto1);
    placementEsrEventDtos.add(placementEsrEventDto2);
    when(placementEsrExportedDtoMapper.placementEsrEventSetToPlacementEsrEventDtoSet(foundEvents))
        .thenReturn(placementEsrEventDtos);

    testObj.populateEsrEventsForPlacementDetail(placementDetailsDto);

    Set<PlacementEsrEventDto> esrEventDtos = placementDetailsDto.getEsrEvents();
    Assert.assertNotNull(esrEventDtos);
    Assert.assertTrue(esrEventDtos.contains(placementEsrEventDto1));
    Assert.assertTrue(esrEventDtos.contains(placementEsrEventDto2));
  }

  @Test
  public void populateEsrEventsShouldFindEventsForThePlacementsAndAddToList() {
    PlacementSummaryDTO placement1 = new PlacementSummaryDTO(), placement2 = new PlacementSummaryDTO();
    placement1.setPlacementId(PLACEMENT_ID);
    placement2.setPlacementId(PLACEMENT2_ID);
    List<PlacementSummaryDTO> placements = Lists.newArrayList(placement1, placement2);

    Placement placement1Mock = mock(Placement.class);
    Placement placement2Mock = mock(Placement.class);
    when(placement1Mock.getId()).thenReturn(PLACEMENT_ID);
    when(placement2Mock.getId()).thenReturn(PLACEMENT2_ID);

    PlacementEsrEvent event1Mock = mock(PlacementEsrEvent.class);
    PlacementEsrEvent event2Mock = mock(PlacementEsrEvent.class);
    when(event1Mock.getPlacement()).thenReturn(placement1Mock);
    when(event2Mock.getPlacement()).thenReturn(placement2Mock);

    Set<PlacementEsrEvent> foundEvents = Sets.newHashSet(event1Mock, event2Mock);
    when(placementEsrEventRepositoryMock
        .findPlacementEsrEventByPlacementIdIn(Lists.newArrayList(PLACEMENT_ID, PLACEMENT2_ID)))
        .thenReturn(foundEvents);

    PlacementEsrEventDto placementEsrEventDto1 = mock(PlacementEsrEventDto.class);
    PlacementEsrEventDto placementEsrEventDto2 = mock(PlacementEsrEventDto.class);
    when(placementEsrExportedDtoMapper.placementEsrEventToPlacementEsrEventDto(event1Mock))
        .thenReturn(placementEsrEventDto1);
    when(placementEsrExportedDtoMapper.placementEsrEventToPlacementEsrEventDto(event2Mock))
        .thenReturn(placementEsrEventDto2);

    testObj.populateEsrEventsForPlacementSummary(placements);

    for (PlacementSummaryDTO placement : placements) {
      Assert.assertNotNull(placement.getEsrEvents());
      Assert.assertTrue(placement.getEsrEvents().contains(placementEsrEventDto1)
      || placement.getEsrEvents().contains(placementEsrEventDto2));
    }
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
    currentPlacement.setLifecycleState(APPROVED);

    PlacementDetailsDTO updatedPlacementDetails = new PlacementDetailsDTO();
    updatedPlacementDetails.setId(existingPlacementId);
    updatedPlacementDetails.setDateFrom(dateOneMonthsAgo);
    updatedPlacementDetails.setLifecycleState(APPROVED);

    Post foundPostMock = mock(Post.class);

    PlacementLog placementLog = new PlacementLog();
    placementLog.setPlacementId(existingPlacementId);
    placementLog.setLifecycleState(APPROVED);
    placementLog.setDateFrom(dateFiveMonthsAgo);
    placementLog.setDateTo(dateOneMonthsAgo);

    when(postRepositoryMock.findPostByPlacementHistoryId(longArgumentCaptor.capture()))
        .thenReturn(Optional.of(foundPostMock));
    when(placementLogServiceImplMock.getLatestLogOfCurrentApprovedPlacement(existingPlacementId))
        .thenReturn(Optional.of(placementLog));

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
    currentPlacement.setLifecycleState(APPROVED);

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
    testObj.createDetails(placementDetailsDto, null);

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

    Placement placement = new Placement();
    placement.setId(1L);

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
    doReturn(null).when(testObj).linkPlacementSpecialties(any(), any());

    // Call the method under test.
    testObj.createDetails(placementDetailsDto, null);

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

    final Placement placement = placementRepositoryMock.findById(placementDetailsDto.getId())
        .orElse(null);
    // Call the method under test.
    PlacementDetailsDTO updatedPlacementDetailsDto = testObj
        .createDetails(placementDetailsDto, null);

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
    when(placementDetailsRepositoryMock.saveAndFlush(updatedPlacementDetails))
        .thenReturn(updatedPlacementDetails);
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(placementDetails))
        .thenReturn(new PlacementDetailsDTO());
    when(placementSpecialtyMapperMock.toDTOs(any())).thenReturn(Collections.emptySet());
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1L);
    when(placementSupervisorRepositoryMock.saveAll(any())).thenReturn(null);
    when(placementSiteMapper.toDto(placementSite1)).thenReturn(placementSiteDto1);
    when(placementSiteMapper.toDto(placementSite2)).thenReturn(placementSiteDto2);

    final Placement placement = placementRepositoryMock.findById(placementDetailsDto.getId())
        .orElse(null);
    // Call the method under test.
    PlacementDetailsDTO updatedPlacementDetailsDto = testObj
        .createDetails(placementDetailsDto, null);

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

  @Test
  public void validateReturnTrueWhenOverlappingPlacementsExist() {
    // prepare mocked data
    String npn = "YHD/RWA01/IMT/LT/003";
    Post mockedPost = new Post();
    mockedPost.setId(1L);
    mockedPost.setNationalPostNumber(npn);

    doReturn(Arrays.asList(mockedPost)).when(postRepositoryMock).findByNationalPostNumber(npn);

    Set<Long> postIds = new HashSet<>();
    postIds.add(1L);
    Placement mockedPlacement = new Placement();
    mockedPlacement.setPost(mockedPost);
    mockedPlacement.setDateFrom(LocalDate.of(2019, 6, 5));
    mockedPlacement.setDateTo(LocalDate.of(2019, 9, 5));

    Set<Placement> mockedPlacementsSet = new HashSet<>();
    mockedPlacementsSet.add(mockedPlacement);
    doReturn(mockedPlacementsSet).when(placementRepositoryMock).findPlacementsByPostIds(postIds);

    boolean result1 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 5, 1),
        LocalDate.of(2019, 6, 5), null);
    boolean result2 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 9, 5),
        LocalDate.of(2019, 10, 10), null);
    boolean result3 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 6, 4),
        LocalDate.of(2019, 9, 6), null);
    boolean result4 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 6, 6),
        LocalDate.of(2019, 9, 4), null);

    Assert.assertThat("When there's one day overlapping - case 1, should return true",
        result1, CoreMatchers.is(true));
    Assert.assertThat("When there's one day overlapping - case 2, should return true",
        result2, CoreMatchers.is(true));
    Assert.assertThat("When the mocked data are fully overlapped, should return true",
        result3, CoreMatchers.is(true));
    Assert.assertThat("When the testing data are fully overlapped, should return true",
        result4, CoreMatchers.is(true));
  }

  @Test
  public void validateReturnFalseWhenNoOverlappingPlacements() {
    // prepare mocked data
    String npn = "YHD/RWA01/IMT/LT/003";
    Post mockedPost = new Post();
    mockedPost.setId(1L);
    mockedPost.setNationalPostNumber(npn);

    doReturn(Arrays.asList(mockedPost)).when(postRepositoryMock).findByNationalPostNumber(npn);

    Set<Long> postIds = new HashSet<>();
    postIds.add(1L);
    Placement mockedPlacement = new Placement();
    mockedPlacement.setPost(mockedPost);
    mockedPlacement.setDateFrom(LocalDate.of(2019, 6, 5));
    mockedPlacement.setDateTo(LocalDate.of(2019, 9, 5));

    Set<Placement> mockedPlacementsSet = new HashSet<>();
    mockedPlacementsSet.add(mockedPlacement);
    doReturn(mockedPlacementsSet).when(placementRepositoryMock).findPlacementsByPostIds(postIds);

    boolean result1 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 5, 1),
        LocalDate.of(2019, 6, 4), null);
    boolean result2 = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 9, 6),
        LocalDate.of(2019, 10, 10), null);

    Assert.assertThat(
        "When the endDate of testing data is ahead of the mocked data, should return false",
        result1, CoreMatchers.is(false));
    Assert.assertThat(
        "When the startDate of testing data is after the mocked data, should return false",
        result2, CoreMatchers.is(false));
  }

  @Test
  public void validateReturnFalseWhenNoPlacementsFound() {
    String npn = "YHD/RWA01/IMT/LT/003";
    Post mockedPost = new Post();
    mockedPost.setId(1L);
    mockedPost.setNationalPostNumber(npn);

    doReturn(Arrays.asList(mockedPost)).when(postRepositoryMock).findByNationalPostNumber(npn);

    Set<Long> postIds = new HashSet<>();
    postIds.add(1L);
    Set<Placement> mockedEmptyPlacementsSet = new HashSet<>();
    doReturn(mockedEmptyPlacementsSet).when(placementRepositoryMock)
        .findPlacementsByPostIds(postIds);
    boolean result = testObj.validateOverlappingPlacements(npn,
        LocalDate.of(2019, 5, 1),
        LocalDate.of(2019, 6, 4), null);

    Assert.assertThat("When there's no placements found, should return false",
        result, CoreMatchers.is(false));
  }

  @Test
  public void isEligibleForChangedDatesNotificationReturnFalseWhenDraftIsNotApproved() {
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(1L);
    placementDetailsDto.setLifecycleState(LifecycleState.DRAFT);

    Placement placement = new Placement();
    placement.setId(1L);
    boolean returnValue = testObj
        .isEligibleForChangedDatesNotification(placementDetailsDto, placement);
    Assert.assertThat(
        "When draft placement is not approved, it is not elegible for ChangedDatesNotification",
        returnValue, CoreMatchers.is(false));
  }

  @Test
  public void isEligibleForChangedDatesNotificationReturnFalseWhenApprovedPlacementGoesBackToDraft() {
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(1L);
    placementDetailsDto.setLifecycleState(LifecycleState.DRAFT);

    Placement placement = new Placement();
    placement.setId(1L);
    placement.setLifecycleState(APPROVED);
    boolean returnValue = testObj
        .isEligibleForChangedDatesNotification(placementDetailsDto, placement);
    Assert.assertThat(
        "When approved placement goes back to draft, it is not elegible for ChangedDatesNotification",
        returnValue, CoreMatchers.is(false));
  }

  @Test
  public void testGetListOfAllDraftPlacementForProgrammeId() {
    Placement placement1 = new Placement();
    placement1.setId(1L);
    placement1.setLifecycleState(LifecycleState.DRAFT);

    Placement placement2 = new Placement();
    placement2.setId(2L);
    placement2.setLifecycleState(LifecycleState.DRAFT);

    Placement placement3 = new Placement();
    placement3.setId(3L);
    placement3.setLifecycleState(APPROVED);

    Post post = new Post();
    post.setId(1L);
    post.setStatus(Status.CURRENT);
    post.setPlacementHistory(Sets.newHashSet(Arrays.asList(placement1, placement2, placement3)));

    Programme programme = new Programme();
    programme.setId(1L);
    programme.setPosts(Sets.newHashSet(Arrays.asList(post)));

    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    when(programmeRepository.findById(any())).thenReturn(Optional.of(programme));
    when(placementDetailsRepositoryMock.findById(any())).thenReturn(Optional.of(placementDetails));
    when(placementDetailsMapperMock.placementDetailsToPlacementDetailsDTO(placementDetails))
        .thenReturn(placementDetailsDto);
    when(placementDetailsDecorator.decorate(placementDetailsDto)).thenReturn(placementDetailsDto);
    List<PlacementDetailsDTO> draftPlacements = testObj
        .getListOfDraftPlacementsByProgrammeId(any());
    Assert.assertThat("Should get the list of all draft placement for the programme id",
        draftPlacements.size(), CoreMatchers.is(2));
  }

  @Test
  public void isEligibleForChangedWholeTimeEquivalentShouldReturnTrueWhenUpdatedPlacementIsEligibleForNotification() {
    LocalDate dateFiveMonthsAgo = LocalDate.now().minusMonths(5);
    Long existingPlacementId = 1L;
    BigDecimal existingWholeTimeEquivalent = new BigDecimal(1.0);
    BigDecimal updatedWholeTimeEquivalent = new BigDecimal(0.5);

    Placement currentPlacement = new Placement();
    currentPlacement.setId(existingPlacementId);
    currentPlacement.setDateFrom(dateFiveMonthsAgo);
    currentPlacement.setLifecycleState(APPROVED);
    currentPlacement.setPlacementWholeTimeEquivalent(existingWholeTimeEquivalent);

    PlacementDetailsDTO updatedPlacementDetails = new PlacementDetailsDTO();
    updatedPlacementDetails.setId(existingPlacementId);
    updatedPlacementDetails.setDateFrom(dateFiveMonthsAgo);
    updatedPlacementDetails.setDateTo(dateFiveMonthsAgo);
    updatedPlacementDetails.setWholeTimeEquivalent(updatedWholeTimeEquivalent);
    updatedPlacementDetails.setWholeTimeEquivalent(updatedWholeTimeEquivalent);
    updatedPlacementDetails.setLifecycleState(APPROVED);

    PlacementLog placementLog = new PlacementLog();
    placementLog.setPlacementId(existingPlacementId);
    placementLog.setLifecycleState(APPROVED);
    placementLog.setDateFrom(dateFiveMonthsAgo);
    placementLog.setDateTo(dateFiveMonthsAgo);

    boolean eligibleForCurrentTraineeWteChangeNotification = testObj
        .isEligibleForCurrentTraineeWteChangeNotification(currentPlacement, updatedPlacementDetails,
            placementLog);

    Assert.assertTrue(eligibleForCurrentTraineeWteChangeNotification);
  }

  @Test
  public void isEligibleForChangedWholeTimeEquivalentShouldDealWithNullCurrentWte() {
    LocalDate dateFiveMonthsAgo = LocalDate.now().minusMonths(5);
    Long existingPlacementId = 1L;
    BigDecimal updatedWholeTimeEquivalent = new BigDecimal(0.5);

    Placement currentPlacement = new Placement();
    currentPlacement.setId(existingPlacementId);
    currentPlacement.setDateFrom(dateFiveMonthsAgo);
    currentPlacement.setLifecycleState(APPROVED);
    currentPlacement.setPlacementWholeTimeEquivalent(null);

    PlacementDetailsDTO updatedPlacementDetails = new PlacementDetailsDTO();
    updatedPlacementDetails.setId(existingPlacementId);
    updatedPlacementDetails.setDateFrom(dateFiveMonthsAgo);
    updatedPlacementDetails.setDateTo(dateFiveMonthsAgo);
    updatedPlacementDetails.setWholeTimeEquivalent(updatedWholeTimeEquivalent);
    updatedPlacementDetails.setWholeTimeEquivalent(updatedWholeTimeEquivalent);
    updatedPlacementDetails.setLifecycleState(APPROVED);

    PlacementLog placementLog = new PlacementLog();
    placementLog.setPlacementId(existingPlacementId);
    placementLog.setLifecycleState(APPROVED);
    placementLog.setDateFrom(dateFiveMonthsAgo);
    placementLog.setDateTo(dateFiveMonthsAgo);

    boolean eligibleForCurrentTraineeWteChangeNotification = testObj
        .isEligibleForCurrentTraineeWteChangeNotification(currentPlacement, updatedPlacementDetails,
            placementLog);

    Assert.assertTrue(eligibleForCurrentTraineeWteChangeNotification);
  }

  @Test
  public void markPlacementAsEsrExportedShouldFindPlacementAndCreateNewEventAgainstIt() {
    PlacementEsrEvent placementEsrEventMock = mock(PlacementEsrEvent.class);
    PlacementEsrEventDto placementEsrExportedDtoMock = mock(PlacementEsrEventDto.class);
    when(placementRepositoryMock.findPlacementById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    when(placementEsrExportedDtoMapper
        .placementEsrEventDtoToPlacementEsrEvent(placementEsrExportedDtoMock))
        .thenReturn(placementEsrEventMock);
    when(placementEsrEventRepositoryMock.save(placementEsrEventArgumentCaptor.capture())).thenReturn(placementEsrEventMock);

    Optional<PlacementEsrEvent> result = testObj
        .markPlacementAsEsrExported(PLACEMENT_ID, placementEsrExportedDtoMock);

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(placementEsrEventMock, result.get());

    PlacementEsrEvent capturedPlacementEvent = placementEsrEventArgumentCaptor.getValue();
    Assert.assertSame(placementEsrEventMock, capturedPlacementEvent);
  }

  @Test
  public void markPlacementAsEsrExportedShouldReturnEmptyOptionalWhenPlacementCannotBeFound() {
    when(placementRepositoryMock.findPlacementById(PLACEMENT_ID)).thenReturn(Optional.empty());
    PlacementEsrEventDto placementEsrExportedDto = new PlacementEsrEventDto();
    placementEsrExportedDto.setPositionNumber(POSITION_NUMBER);
    placementEsrExportedDto.setPositionId(POSITION_ID);
    placementEsrExportedDto.setPlacementId(PLACEMENT_ID);
    placementEsrExportedDto.setFilename(ESR_FILENAME_TXT);
    placementEsrExportedDto.setExportedAt(new Date(111L));

    Optional<PlacementEsrEvent> result = testObj
        .markPlacementAsEsrExported(PLACEMENT_ID, placementEsrExportedDto);

    Assert.assertFalse(result.isPresent());
    verifyNoMoreInteractions(placementEsrEventRepositoryMock);
  }
}
