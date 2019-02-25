package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSpecialtyMapper;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlacementServiceImplTest {

  public static final long PLACEMENT_ID = 1L;
  private static final BigInteger bigInteger = BigInteger.ONE;
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
  private PlacementSpecialtyMapper placementSpecialtyMapperMock;
  @Mock
  private Placement placementMock;
  @Mock
  private PlacementDTO placementDTOMock;
  @Mock
  private javax.persistence.Query queryMock;
  @Mock
  private SqlQuerySupplier sqlQuerySupplierMock;
  @Mock
  private EntityManager entityManagerMock;
  @Captor
  private ArgumentCaptor<LocalDate> toDateCaptor;
  @Mock
  private Clock clock;

  public static PlacementSummaryDTO createPlacementSummaryDTO() {
    return new PlacementSummaryDTO(null, null, bigInteger,
      "Elbows", bigInteger, "In Post", "CURRENT", "Joe", "Bloggs",
      bigInteger, bigInteger, null);
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
    when(placementMapperMock.placementToPlacementDTO(placementMock)).thenReturn(placementDTOMock);

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
    BigInteger id1 = BigInteger.valueOf(1);
    BigInteger id2 = BigInteger.valueOf(2);
    BigInteger id3 = BigInteger.valueOf(3);
    BigInteger id4 = BigInteger.valueOf(4);
    BigInteger id5 = BigInteger.valueOf(5);

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

    for (int i = 6; i < 2000; i++) {
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId(BigInteger.valueOf(i));

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE traineeId = :traineeId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY))
      .thenReturn(sqlQueryMock);
    when(entityManagerMock
      .createNativeQuery(sqlQueryMock, PlacementServiceImpl.PLACEMENTS_SUMMARY_MAPPER))
      .thenReturn(queryMock);
    when(queryMock.setParameter("traineeId", traineeId)).thenReturn(queryMock);
    when(queryMock.getResultList()).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForTrainee(traineeId);

    int sizeOfResult = result.size();
    Assert.assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult - 2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult - 1).getDateTo());
  }

  @Test
  public void shouldReturnPlacementsForAPostInOrder() throws Exception {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
    Date latest_date = simpleDateFormat.parse("2090/12/01");
    Date second_latest_date = simpleDateFormat.parse("2020/12/01");
    Date earliest_date = simpleDateFormat.parse("2010/12/01");
    Date bulk_date = simpleDateFormat.parse("2018/12/01");
    Long postId = 1L;
    BigInteger id1 = BigInteger.valueOf(1);
    BigInteger id2 = BigInteger.valueOf(2);
    BigInteger id3 = BigInteger.valueOf(3);
    BigInteger id4 = BigInteger.valueOf(4);
    BigInteger id5 = BigInteger.valueOf(5);

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

    for (int i = 6; i < 2000; i++) {
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId(BigInteger.valueOf(i));

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE p.postId = :postId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY))
      .thenReturn(sqlQueryMock);
    when(entityManagerMock
      .createNativeQuery(sqlQueryMock, PlacementServiceImpl.PLACEMENTS_SUMMARY_MAPPER))
      .thenReturn(queryMock);
    when(queryMock.setParameter("postId", postId)).thenReturn(queryMock);
    when(queryMock.getResultList()).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForPost(postId);

    int sizeOfResult = result.size();
    Assert.assertTrue(result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertTrue(result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult - 2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult - 1).getDateTo());
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
    doNothing().when(placementSupervisorRepositoryMock).deleteAllByIdPlacementId(1l);
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
}
