package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogType;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementLog;
import com.transformuk.hee.tis.tcs.service.repository.PlacementLogRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlacementLogServiceImpl implements PlacementLogService {

  @Autowired
  private PlacementLogRepository placementLogRepository;
  @Autowired
  private Clock clock;

  @Transactional
  @Override
  public PlacementLog placementLog(PlacementDetails placementDetails, PlacementLogType logType) {
    PlacementLog placementLog = null;

    switch (logType) {
      case CREATE:
        placementLog = buildPlacementLog(placementDetails, logType);
        break;
      case UPDATE:
        updatePreviousLogForPlacementId(placementDetails.getId());
        placementLog = buildPlacementLog(placementDetails, logType);
        break;
      case DELETE:
        updatePreviousLogForPlacementId(placementDetails.getId());
        placementLog = buildPlacementLog(placementDetails, logType);
        placementLog.setValidDateTo(placementLog.getValidDateFrom());
    }
    return placementLogRepository.save(placementLog);
  }

  @Override
  public Optional<PlacementLog> getLatestLogOfCurrentApprovedPlacement(Long placementId) {
    Optional<PlacementLog> optionalPreLog = placementLogRepository
        .findLatestLogOfCurrentApprovedPlacement(placementId);
    return optionalPreLog;
  }

  private void updatePreviousLogForPlacementId(Long placementId) {
    List<PlacementLog> previousLogs = placementLogRepository.findLatestLogOfCurrentPlacement(placementId);
    LocalDateTime now = LocalDateTime.now(clock);
    previousLogs.forEach(placementLog -> {
      placementLog.setValidDateTo(now);
      placementLogRepository.save(placementLog);
    });
  }

  private PlacementLog buildPlacementLog(PlacementDetails placementDetails,
                                         PlacementLogType logType) {
    PlacementLog placementLog = new PlacementLog();
    placementLog.setDateFrom(placementDetails.getDateFrom());
    placementLog.setDateTo(placementDetails.getDateTo());
    placementLog.setLifecycleState(placementDetails.getLifecycleState());
    placementLog.setPlacementId(placementDetails.getId());
    placementLog.setLogType(logType);
    placementLog.setValidDateFrom(LocalDateTime.now(clock));

    return placementLog;
  }

  @Override
  public void addLogForExistingPlacement(PlacementDetails existingPlacementDetails) {
    List<PlacementLog> preLogs = placementLogRepository
        .findLatestLogOfCurrentPlacement(existingPlacementDetails.getId());
    if (preLogs.isEmpty()) {
      PlacementLog placementLog = buildPlacementLog(existingPlacementDetails, null);
      placementLogRepository.save(placementLog);
    }
  }
}
