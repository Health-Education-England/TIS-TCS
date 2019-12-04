package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogType;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementLog;

import java.util.Optional;

public interface PlacementLogService {


  /**
   *
   * @param placementDetails
   * @param logType
   * @return
   */
  PlacementLog placementLog(PlacementDetails placementDetails, PlacementLogType logType);

  /**
   *
   * @param placementId
   * @return
   */
  Optional<PlacementLog> getLatestLogOfCurrentApprovedPlacement(Long placementId);
}
