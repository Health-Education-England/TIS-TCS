package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrExportedDto;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import java.time.Instant;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementEsrExportedDtoMapperTest {

  public static final String FILENAME = "filename";
  public static final Date FIXED_DATE = new Date(111L);
  public static final Long PLACEMENT_ID = 1111L;
  public static final Long POSITION_ID = 22222L;
  public static final Long POSITION_NUMBER = 4444L;

  @Autowired
  private PlacementEsrExportedDtoMapper placementEsrExportedDtoMapper;

  @Test
  public void mapperShouldConvertDtoToEntity(){
    PlacementEsrExportedDto dto = new PlacementEsrExportedDto();
    dto.setExportedAt(FIXED_DATE);
    dto.setFilename(FILENAME);
    dto.setPositionId(POSITION_ID);
    dto.setPositionNumber(POSITION_NUMBER);

    PlacementEsrEvent result = placementEsrExportedDtoMapper
        .placementEsrExportedDtoToPlacementEsrEvent(dto);

    Assert.assertEquals(FIXED_DATE, result.getEventDateTime());
    Assert.assertEquals(FILENAME, result.getFilename());
    Assert.assertEquals(POSITION_ID, result.getPositionId());
    Assert.assertEquals(POSITION_NUMBER, result.getPositionNumber());

  }
}
