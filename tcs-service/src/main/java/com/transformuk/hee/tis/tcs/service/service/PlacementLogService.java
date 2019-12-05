package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogType;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementLog;

import java.util.Optional;

public interface PlacementLogService {


  /**
   * add log in the PlacementLog table
   * @param placementDetails The placement details which users operates
   * @param logType Users' operation to the placement. Should be in (CREATE, UPDATE, DELETE).
   * @return The latest log
   */
  PlacementLog placementLog(PlacementDetails placementDetails, PlacementLogType logType);

  /**
   * get the latest log of current placement whose lifecycleState is approved
   * @param placementId id of the placement
   * @return the latest log
   */
  Optional<PlacementLog> getLatestLogOfCurrentApprovedPlacement(Long placementId);
}
