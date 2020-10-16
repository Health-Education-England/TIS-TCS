package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrEventDto;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementEsrEventStatus;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementEsrEventDtoMapperTest {

  public static final String FILENAME = "filename";
  public static final Date FIXED_DATE = new Date(111L);
  public static final Long PLACEMENT_ID = 1111L;
  public static final Long POSITION_ID = 22222L;
  public static final Long POSITION_NUMBER = 4444L;

  @Autowired
  private PlacementEsrEventDtoMapper placementEsrExportedDtoMapper;

  @Test
  public void mapperShouldConvertDtoToEntity(){
    PlacementEsrEventDto dto = new PlacementEsrEventDto();
    dto.setExportedAt(FIXED_DATE);
    dto.setFilename(FILENAME);
    dto.setPositionId(POSITION_ID);
    dto.setPositionNumber(POSITION_NUMBER);

    PlacementEsrEvent result = placementEsrExportedDtoMapper
        .placementEsrEventDtoToPlacementEsrEvent(dto);

    Assert.assertEquals(FIXED_DATE, result.getEventDateTime());
    Assert.assertEquals(FILENAME, result.getFilename());
    Assert.assertEquals(POSITION_ID, result.getPositionId());
    Assert.assertEquals(POSITION_NUMBER, result.getPositionNumber());
  }

  @Test
  public void mapperShouldConvertEntityToDto() {
    PlacementEsrEvent entity = new PlacementEsrEvent();
    entity.setStatus(PlacementEsrEventStatus.EXPORTED);
    entity.setPositionId(POSITION_ID);
    entity.setPositionNumber(POSITION_NUMBER);
    entity.setFilename(FILENAME);
    entity.setEventDateTime(FIXED_DATE);

    PlacementEsrEventDto result = placementEsrExportedDtoMapper
        .placementEsrEvenToPlacementEsrEventDto(entity);

    Assert.assertEquals(PlacementEsrEventStatus.EXPORTED.toString(), result.getStatus());
    Assert.assertEquals(POSITION_ID, result.getPositionId());
    Assert.assertEquals(POSITION_NUMBER, result.getPositionNumber());
    Assert.assertEquals(FILENAME, result.getFilename());
    Assert.assertEquals(FIXED_DATE, result.getExportedAt());
  }
}
