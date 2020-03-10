package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class PlacementSupervisorDTO implements Serializable {

  private static final long serialVersionUID = -1604065532486158813L;

  private PersonLiteDTO person;
  private Integer type;
  private Long placementId;
}
