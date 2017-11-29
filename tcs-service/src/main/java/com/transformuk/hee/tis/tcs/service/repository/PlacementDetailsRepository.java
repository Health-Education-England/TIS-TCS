package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Placement entity.
 */
@SuppressWarnings("unused")
public interface PlacementDetailsRepository extends JpaRepository<PlacementDetails, Long> {

}
