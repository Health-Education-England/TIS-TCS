package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Placement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Placement entity.
 */
@SuppressWarnings("unused")
public interface PlacementRepository extends JpaRepository<Placement, Long> {

}
