package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.FundingComponents;
import com.transformuk.hee.tis.tcs.service.model.PlacementLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FundingComponents entity.
 */
@SuppressWarnings("unused")
public interface PlacementLogRepository extends JpaRepository<PlacementLog, Long> {

}
