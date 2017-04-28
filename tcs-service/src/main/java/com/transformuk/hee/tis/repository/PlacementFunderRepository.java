package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.PlacementFunder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PlacementFunder entity.
 */
@SuppressWarnings("unused")
public interface PlacementFunderRepository extends JpaRepository<PlacementFunder, Long> {

}
