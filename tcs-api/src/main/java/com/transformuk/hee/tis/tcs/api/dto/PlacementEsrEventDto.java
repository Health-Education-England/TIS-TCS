package com.transformuk.hee.tis.tcs.api.dto;

import java.util.Date;
import lombok.Data;

@Data
public class PlacementEsrEventDto {
  private Date exportedAt;
  private String filename;
  private Long placementId;
  private Long positionNumber;
  private Long positionId;
  private String status;
}
