package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the FundingComponents entity.
 */
public interface PlacementLogRepository extends JpaRepository<PlacementLog, Long> {

  @Query(value =
      "SELECT * FROM PlacementLog WHERE placementId = :placementId AND validDateTo IS NULL"
      , nativeQuery = true)
  public Optional<PlacementLog> findLatestLogOfCurrentPlacement(
      @Param("placementId") Long placementId);

  @Query(value =
      "SELECT * FROM PlacementLog WHERE id =" +
          "(SELECT max(id) FROM PlacementLog WHERE lifecycleState = 'APPROVED' AND placementId = :placementId)"
      , nativeQuery = true)
  public Optional<PlacementLog> findLatestLogOfCurrentApprovedPlacement(
      @Param("placementId") Long placementId);
}
