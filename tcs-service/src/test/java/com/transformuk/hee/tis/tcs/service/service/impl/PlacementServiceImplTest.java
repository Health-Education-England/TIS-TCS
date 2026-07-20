package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Person;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class PlacementServiceImplTest {

  public static final Long POSITION_NUMBER = 1111L;
  public static final Long POSITION_ID = 2222L;
  public static final String ESR_FILENAME_TXT = "esr_filename.txt";
  private static final Long PLACEMENT_ID = 1L, PLACEMENT2_ID = 2L;
  private static final Long number = 1L;
  private static final String string = "fooo";
  private static final Long PERSON_ID = 1L;
  private static final Long POST_ID = 2L;
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
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;
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
  @Captor
  private ArgumentCaptor<PlacementDeletedEvent> placementDeletedEventCaptor;
  @Captor
  private ArgumentCaptor<PlacementSavedEvent> placementSavedEventCaptor;

  static PlacementSummaryDTO createPlacementSummaryDTO() {
    return new PlacementSummaryDTO(null, null, number, string, "Elbows", number, string, "In Post",
        "CURRENT", "Joe", "Bloggs", "Joe", "Bloggs", number, "emailId", "F1", number, string, null,
        null, number, null, new HashSet<>());
  }

  @BeforeEach
  void setup() {
    lenient().when(clock.instant()).thenReturn(Instant.now());
    lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
  }

  @Test
  void closePlacementShouldClosePlacementBySettingToDate() {
    when(placementRepositoryMock.findById(PLACEMENT_ID)).thenReturn(Optional.of(placementMock));
    doNothing().when(placementMock).setDateTo(toDateCaptor.capture());
    when(placementRepositoryMock.saveAndFlush(placementMock)).thenReturn(placementMock);
    when(placementMapperMock.placementToPlacementDTO(eq(placementMock), anyMap()))
        .thenReturn(placementDTOMock);

    PlacementDTO result = testObj.closePlacement(PLACEMENT_ID);

    assertEquals(placementDTOMock, result);

    LocalDate toDateCapture = toDateCaptor.getValue();
    assertEquals(LocalDate.now().minusDays(1), toDateCapture);
  }

  @Test
  void shouldReturnPlacementsForATraineeInOrder() throws Exception {

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
    assertEquals(wte, result.get(0).getPlacementWholeTimeEquivalent());
    assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    assertNull(result.get(sizeOfResult - 2).getDateTo());
    assertNull(result.get(sizeOfResult - 1).getDateTo());

    Map<String, Object> capturedParams = mapArgumentCaptor.getValue();
    assertTrue(capturedParams.containsKey("traineeId"));

    PlacementRowMapper capturedRowMapper = placementRowMapperArgumentCaptor.getValue();
    assertNotNull(capturedRowMapper);
  }

  @Test
  void populateEsrEventsShouldFindEventsForPlacementDetails() {
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
    assertNotNull(esrEventDtos);
    assertTrue(esrEventDtos.contains(placementEsrEventDto1));
    assertTrue(esrEventDtos.contains(placementEsrEventDto2));
  }

  @Test
  void populateEsrEventsShouldFindEventsForThePlacementsAndAddToList() {
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
      assertNotNull(placement.getEsrEvents());
      assertTrue(placement.getEsrEvents().contains(placementEsrEventDto1)
          || placement.getEsrEvents().contains(placementEsrEventDto2));
    }
  }

  @Test
  void shouldReturnPlacementsForAPostInOrder() throws Exception {

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
    assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    assertNull(result.get(sizeOfResult - 2).getDateTo());
    assertNull(result.get(sizeOfResult - 1).getDateTo());

    Map<String, Object> capturedParams = mapArgumentCaptor.getValue();
    assertTrue(capturedParams.containsKey("postId"));

    PlacementRowMapper capturedRowMapper = placementRowMapperArgumentCaptor.getValue();
    assertNotNull(capturedRowMapper);
  }

  @Test
  void placementForPostShouldLimitPostsIfMoreThan1k() {
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

    assertTrue(result.size() <= 1000);
  }

  @Test
  void isEligibleForChangedDatesNotificationShouldReturnTrueWhenUpdatedPlacementIsEligibleForNotification() {
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

    assertTrue(result);

    Long capturedPlacementId = longArgumentCaptor.getValue();
    assertEquals(existingPlacementId, capturedPlacementId);
  }

  @Test
  void isEligibleForChangedDatesNotificationShouldReturnFalseWhenCurrentAndUpdatedPlacementFromDatesAreTheSame() {
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

    assertFalse(result);

    verifyNoInteractions(postRepositoryMock);
  }

  /**
   * Test that the placement's addedDate is populated and the amendedDate is not populated when the
   * placement ID is null.
   */
  @Test
  void testCreateDetails_placementIdNull_addedDatePopulatedAmendedDateNotPopulated() {
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
    assertEquals(LocalDateTime.now(clock), placementDetails.getAddedDate());
    assertNull(placementDetails.getAmendedDate());
  }

  /**
   * Test that the placement's addedDate is not populated and the amendedDate is populated when the
   * placement ID is not null.
   */
  @Test
  void testCreateDetails_placementIdNotNull_addedDateNotPopulatedAmendedDatePopulated() {
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
    assertNull(placementDetails.getAddedDate());
    assertEquals(LocalDateTime.now(clock), placementDetails.getAmendedDate());
  }

  /**
   * Test that no sites are contained in the DTO when no sites are given.
   */
  @Test
  void testCreateDetails_noSites_noSites() {
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
    assertEquals(0, sites.size());
  }

  /**
   * Test that sites are contained in the DTO when sites are given.
   */
  @Test
  void testCreateDetails_hasSites_hasSites() {
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
    assertEquals(2,  sites.size());

    for (PlacementSiteDTO site : sites) {
      assertEquals(PlacementSiteType.OTHER, site.getPlacementSiteType());
      assertEquals(1L, site.getPlacementId());
    }
  }

  @Test
  void validateReturnTrueWhenOverlappingPlacementsExist() {
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

    assertTrue(result1);
    assertTrue(result2);
    assertTrue(result3);
    assertTrue(result4);
  }

  @Test
  void validateReturnFalseWhenNoOverlappingPlacements() {
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

    assertFalse(result1);
    assertFalse(result2);
  }

  @Test
  void validateReturnFalseWhenNoPlacementsFound() {
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

    assertFalse(result);
  }

  @Test
  void isEligibleForChangedDatesNotificationReturnFalseWhenDraftIsNotApproved() {
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(1L);
    placementDetailsDto.setLifecycleState(LifecycleState.DRAFT);

    Placement placement = new Placement();
    placement.setId(1L);
    boolean returnValue = testObj
        .isEligibleForChangedDatesNotification(placementDetailsDto, placement);

    assertFalse(returnValue);
  }

  @Test
  void isEligibleForChangedDatesNotificationReturnFalseWhenApprovedPlacementGoesBackToDraft() {
    PlacementDetailsDTO placementDetailsDto = new PlacementDetailsDTO();
    placementDetailsDto.setId(1L);
    placementDetailsDto.setLifecycleState(LifecycleState.DRAFT);

    Placement placement = new Placement();
    placement.setId(1L);
    placement.setLifecycleState(APPROVED);
    boolean returnValue = testObj
        .isEligibleForChangedDatesNotification(placementDetailsDto, placement);

    assertFalse(returnValue);
  }

  @Test
  void testGetListOfAllDraftPlacementForProgrammeId() {
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

    assertEquals(2, draftPlacements.size());
  }

  @Test
  void isEligibleForChangedWholeTimeEquivalentShouldReturnTrueWhenUpdatedPlacementIsEligibleForNotification() {
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

    assertTrue(eligibleForCurrentTraineeWteChangeNotification);
  }

  @Test
  void isEligibleForChangedWholeTimeEquivalentShouldDealWithNullCurrentWte() {
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

    assertTrue(eligibleForCurrentTraineeWteChangeNotification);
  }

  @Test
  void markPlacementAsEsrExportedShouldFindPlacementAndCreateNewEventAgainstIt() {
    PlacementEsrEvent placementEsrEventMock = mock(PlacementEsrEvent.class);
    PlacementEsrEventDto placementEsrExportedDtoMock = mock(PlacementEsrEventDto.class);
    when(placementRepositoryMock.findPlacementById(PLACEMENT_ID))
        .thenReturn(Optional.of(placementMock));
    when(placementEsrExportedDtoMapper
        .placementEsrEventDtoToPlacementEsrEvent(placementEsrExportedDtoMock))
        .thenReturn(placementEsrEventMock);
    when(placementEsrEventRepositoryMock.save(placementEsrEventArgumentCaptor.capture()))
        .thenReturn(placementEsrEventMock);

    Optional<PlacementEsrEvent> result = testObj
        .markPlacementAsEsrExported(PLACEMENT_ID, placementEsrExportedDtoMock);

    assertTrue(result.isPresent());
    assertEquals(placementEsrEventMock, result.get());

    PlacementEsrEvent capturedPlacementEvent = placementEsrEventArgumentCaptor.getValue();
    assertSame(placementEsrEventMock, capturedPlacementEvent);
  }

  @Test
  void markPlacementAsEsrExportedShouldReturnEmptyOptionalWhenPlacementCannotBeFound() {
    when(placementRepositoryMock.findPlacementById(PLACEMENT_ID)).thenReturn(Optional.empty());
    PlacementEsrEventDto placementEsrExportedDto = new PlacementEsrEventDto();
    placementEsrExportedDto.setPositionNumber(POSITION_NUMBER);
    placementEsrExportedDto.setPositionId(POSITION_ID);
    placementEsrExportedDto.setPlacementId(PLACEMENT_ID);
    placementEsrExportedDto.setFilename(ESR_FILENAME_TXT);
    placementEsrExportedDto.setExportedAt(new Date(111L));

    Optional<PlacementEsrEvent> result = testObj
        .markPlacementAsEsrExported(PLACEMENT_ID, placementEsrExportedDto);

    assertFalse(result.isPresent());
    verifyNoMoreInteractions(placementEsrEventRepositoryMock);
  }

  @Test
  void shouldGetCurrentPlacementsForPersonId() {
    Long personId = PERSON_ID;
    when(clock.instant()).thenReturn(Instant.parse("2026-07-16T00:00:00Z"));
    when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    LocalDate currentDate = LocalDate.now(clock);

    Placement placement1 = new Placement();
    placement1.setId(PLACEMENT_ID);
    Placement placement2 = new Placement();
    placement2.setId(PLACEMENT2_ID);
    List<Placement> placements = Lists.newArrayList(placement1, placement2);

    PlacementDTO placementDto1 = new PlacementDTO();
    placementDto1.setId(PLACEMENT_ID);
    PlacementDTO placementDto2 = new PlacementDTO();
    placementDto2.setId(PLACEMENT2_ID);
    List<PlacementDTO> expectedDtos = Lists.newArrayList(placementDto1, placementDto2);

    when(placementRepositoryMock.findAllCurrentPlacementsForTrainee(personId, currentDate))
        .thenReturn(placements);
    when(placementMapperMock.placementsToPlacementDTOs(placements, null)).thenReturn(expectedDtos);

    List<PlacementDTO> result = testObj.getCurrentPlacementsForPersonId(personId);

    assertEquals(expectedDtos, result);
    verify(placementRepositoryMock).findAllCurrentPlacementsForTrainee(personId, currentDate);
    verify(placementMapperMock).placementsToPlacementDTOs(placements, null);
  }

  @Test
  void deleteShouldPublishPlacementDeletedEvent() {
    Placement placement = new Placement();
    placement.setId(PLACEMENT_ID);
    placement.setLifecycleState(LifecycleState.DRAFT);
    Post post = new Post();
    post.setId(10L);
    placement.setPost(post);
    com.transformuk.hee.tis.tcs.service.model.Person trainee =
        new com.transformuk.hee.tis.tcs.service.model.Person();
    trainee.setId(20L);
    placement.setTrainee(trainee);

    PlacementDTO oldPlacementDto = new PlacementDTO();
    oldPlacementDto.setId(PLACEMENT_ID);

    when(placementLogServiceImplMock.getLatestLogOfCurrentApprovedPlacement(PLACEMENT_ID))
        .thenReturn(Optional.empty());
    when(placementRepositoryMock.findById(PLACEMENT_ID)).thenReturn(Optional.of(placement));
    when(placementRepositoryMock.getOne(PLACEMENT_ID)).thenReturn(placement);
    when(placementMapperMock.placementToPlacementDTO(placement, null)).thenReturn(oldPlacementDto);

    testObj.delete(PLACEMENT_ID);

    verify(applicationEventPublisher).publishEvent(placementDeletedEventCaptor.capture());
    PlacementDeletedEvent event = placementDeletedEventCaptor.getValue();
    assertEquals(oldPlacementDto, event.getPlacementDto());
    verify(placementRepositoryMock).delete(placement);
  }

  @Test
  void saveDetailsShouldPublishPlacementSavedEvent() {
    PlacementDetailsDTO placementDetailsDTO = new PlacementDetailsDTO();
    placementDetailsDTO.setId(PLACEMENT_ID);

    Placement placement = new Placement();
    placement.setId(PLACEMENT_ID);
    placement.setSpecialties(new HashSet<>());

    Post post = new Post();
    post.setId(POST_ID);
    placement.setPost(post);

    Person trainee = new Person();
    trainee.setId(PERSON_ID);
    placement.setTrainee(trainee);

    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(PLACEMENT_ID);

    PlacementDetailsDTO updatedPlacementDetailsDTO = new PlacementDetailsDTO();
    updatedPlacementDetailsDTO.setId(PLACEMENT_ID);

    PlacementDTO resultDTO = new PlacementDTO();
    resultDTO.setId(PLACEMENT_ID);

    PlacementDTO existingPlacementDto = new PlacementDTO();
    existingPlacementDto.setId(PLACEMENT_ID);

    when(placementRepositoryMock.findById(PLACEMENT_ID)).thenReturn(Optional.of(placement));
    when(placementMapperMock.placementToPlacementDTO(placement, null)).thenReturn(existingPlacementDto);
    when(placementDetailsMapperMock.placementDetailsDTOToPlacementDetails(placementDetailsDTO))
        .thenReturn(placementDetails);
    when(placementRepositoryMock.saveAndFlush(placement)).thenReturn(placement);
    when(placementMapperMock.placementToPlacementDTO(eq(placement), anyMap()))
        .thenReturn(resultDTO);
    doReturn(updatedPlacementDetailsDTO).when(testObj).createDetails(placementDetailsDTO, placement);
    when(placementDetailsMapperMock.placementDetailsDtoToPlacementDto(updatedPlacementDetailsDTO))
        .thenReturn(resultDTO);

    testObj.saveDetails(placementDetailsDTO);

    verify(applicationEventPublisher).publishEvent(placementSavedEventCaptor.capture());
    PlacementSavedEvent event = placementSavedEventCaptor.getValue();
    assertNotNull(event);
    assertEquals(resultDTO, event.getPlacementDTO());
    assertEquals(existingPlacementDto, event.getPreviousPlacementDto());
  }

  @Test
  void saveListShouldPublishPlacementSavedEventForEachPlacement() {
    PlacementDTO placementDTO1 = new PlacementDTO();
    placementDTO1.setId(PLACEMENT_ID);
    PlacementDTO placementDTO2 = new PlacementDTO();
    placementDTO2.setId(PLACEMENT2_ID);
    List<PlacementDTO> placementDTOs = Lists.newArrayList(placementDTO1, placementDTO2);

    Placement placement1 = new Placement();
    placement1.setId(PLACEMENT_ID);
    Placement placement2 = new Placement();
    placement2.setId(PLACEMENT2_ID);
    List<Placement> placements = Lists.newArrayList(placement1, placement2);

    PlacementDTO resultDTO1 = new PlacementDTO();
    resultDTO1.setId(PLACEMENT_ID);
    PlacementDTO resultDTO2 = new PlacementDTO();
    resultDTO2.setId(PLACEMENT2_ID);
    List<PlacementDTO> resultDTOs = Lists.newArrayList(resultDTO1, resultDTO2);

    when(placementMapperMock.placementDTOsToPlacements(placementDTOs)).thenReturn(placements);
    when(placementRepositoryMock.saveAll(placements)).thenReturn(placements);
    when(placementMapperMock.placementsToPlacementDTOs(eq(placements), anyMap()))
        .thenReturn(resultDTOs);

    testObj.save(placementDTOs);

    verify(applicationEventPublisher, times(2)).publishEvent(placementSavedEventCaptor.capture());
    List<PlacementSavedEvent> events = placementSavedEventCaptor.getAllValues();
    assertEquals(2, events.size());
    assertEquals(placementDTO1, events.get(0).getPlacementDTO());
    assertNull(events.get(0).getPreviousPlacementDto());
    assertEquals(placementDTO2, events.get(1).getPlacementDTO());
    assertNull(events.get(1).getPreviousPlacementDto());
  }

  @Test
  void linkPlacementSpecialtiesShouldPublishPlacementSavedEventWhenNewPlacement() {
    PlacementDetailsDTO placementDetailsDTO = new PlacementDetailsDTO();
    placementDetailsDTO.setId(null);  // null means this is a new placement

    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    Placement placement = new Placement();
    placement.setId(1L);

    PlacementDTO resultDTO = new PlacementDTO();
    resultDTO.setId(1L);

    when(placementRepositoryMock.findById(1L)).thenReturn(Optional.of(placement));
    when(placementRepositoryMock.save(placement)).thenReturn(placement);
    when(placementMapperMock.placementToPlacementDTO(eq(placement), anyMap()))
        .thenReturn(resultDTO);

    testObj.linkPlacementSpecialties(placementDetailsDTO, placementDetails);

    verify(applicationEventPublisher).publishEvent(placementSavedEventCaptor.capture());
    PlacementSavedEvent event = placementSavedEventCaptor.getValue();
    assertNotNull(event);
    assertEquals(resultDTO, event.getPlacementDTO());
    assertNull(event.getPreviousPlacementDto());
  }

  @Test
  void linkPlacementSpecialtiesShouldNotPublishPlacementSavedEventWhenExistingPlacement() {
    PlacementDetailsDTO placementDetailsDTO = new PlacementDetailsDTO();
    placementDetailsDTO.setId(1L);  // non-null means this is an existing placement

    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setId(1L);

    Placement placement = new Placement();
    placement.setId(1L);

    when(placementRepositoryMock.findById(1L)).thenReturn(Optional.of(placement));
    when(placementRepositoryMock.save(placement)).thenReturn(placement);

    testObj.linkPlacementSpecialties(placementDetailsDTO, placementDetails);

    verify(applicationEventPublisher, times(0)).publishEvent(any(PlacementSavedEvent.class));
  }
}
