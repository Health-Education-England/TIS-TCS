package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
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
  @Mock
  private javax.persistence.Query queryMock;
  @Mock
  private SqlQuerySupplier sqlQuerySupplierMock;
  @Mock
  private EntityManager entityManagerMock;
  @Captor
  private ArgumentCaptor<LocalDate> toDateCaptor;

  private static final BigInteger bigInteger = BigInteger.ONE;

  public static PlacementSummaryDTO createPlacementSummaryDTO() {
    return new PlacementSummaryDTO(null,null,bigInteger,
        "Elbows",bigInteger,"In Post","CURRENT","Joe", "Bloggs",
        bigInteger,bigInteger,null);
  }

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
  @Test
  public void shouldReturnPlacementsForATraineeInOrder () throws Exception {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
    Date latest_date = simpleDateFormat.parse("2090/12/01");
    Date second_latest_date = simpleDateFormat.parse("2020/12/01");
    Date earliest_date = simpleDateFormat.parse("2010/12/01");
    Date bulk_date = simpleDateFormat.parse("2018/12/01");
    Date null_date = null;
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
    placement_null.setDateTo(null_date);
    placement_null.setPlacementId(id4);
    placement_null_2.setDateTo(null_date);
    placement_null_2.setPlacementId(id5);

    List<PlacementSummaryDTO> placements = Lists.newArrayList(placement_second_latest,placement_earliest,placement_null,
        placement_latest,placement_null_2);

    for(int i = 6; i < 2000; i++){
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId(BigInteger.valueOf(i));

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE traineeId = :traineeId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY)).thenReturn(sqlQueryMock);
    when(entityManagerMock.createNativeQuery(sqlQueryMock,PlacementServiceImpl.PLACEMENTS_SUMMARY_MAPPER)).thenReturn(queryMock);
    when(queryMock.setParameter("traineeId",traineeId)).thenReturn(queryMock);
    when(queryMock.getResultList()).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForTrainee(traineeId);

    int sizeOfResult = result.size();
    Assert.assertEquals(true, result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertEquals(true, result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult-2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult-1).getDateTo());
  }

  @Test
  public void shouldReturnPlacementsForAPostInOrder () throws Exception {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
    Date latest_date = simpleDateFormat.parse("2090/12/01");
    Date second_latest_date = simpleDateFormat.parse("2020/12/01");
    Date earliest_date = simpleDateFormat.parse("2010/12/01");
    Date bulk_date = simpleDateFormat.parse("2018/12/01");
    Date null_date = null;
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
    placement_null.setDateTo(null_date);
    placement_null.setPlacementId(id4);
    placement_null_2.setDateTo(null_date);
    placement_null_2.setPlacementId(id5);

    List<PlacementSummaryDTO> placements = Lists.newArrayList(placement_second_latest,placement_earliest,placement_null,
        placement_latest,placement_null_2);

    for(int i = 6; i < 2000; i++){
      PlacementSummaryDTO placement = new PlacementSummaryDTO();
      placement.setDateTo(bulk_date);
      placement.setPlacementId(BigInteger.valueOf(i));

      placements.add(placement);
    }

    String sqlQueryMock = "SELECT * FROM PLACEMENT WHERE p.postId = :postId";

    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY)).thenReturn(sqlQueryMock);
    when(entityManagerMock.createNativeQuery(sqlQueryMock,PlacementServiceImpl.PLACEMENTS_SUMMARY_MAPPER)).thenReturn(queryMock);
    when(queryMock.setParameter("postId",postId)).thenReturn(queryMock);
    when(queryMock.getResultList()).thenReturn(placements);

    List<PlacementSummaryDTO> result = testObj.getPlacementForPost(postId);

    int sizeOfResult = result.size();
    Assert.assertEquals(true, result.get(0).getDateTo().after(result.get(1).getDateTo()));
    Assert.assertEquals(true, result.get(1).getDateTo().after(result.get(2).getDateTo()));
    Assert.assertNull(result.get(sizeOfResult-2).getDateTo());
    Assert.assertNull(result.get(sizeOfResult-1).getDateTo());
  }

}