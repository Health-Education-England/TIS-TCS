package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementEsrEventStatus;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class PlacementEsrEventDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private Date exportedAt;
  private String filename;
  private Long placementId;
  private Long positionNumber;
  private Long positionId;
  private PlacementEsrEventStatus status;
}
