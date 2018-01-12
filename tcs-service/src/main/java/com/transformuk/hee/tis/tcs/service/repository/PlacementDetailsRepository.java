package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Placement entity.
 */
public interface PlacementDetailsRepository extends JpaRepository<PlacementDetails, Long>, JpaSpecificationExecutor<PlacementDetails> {

}
