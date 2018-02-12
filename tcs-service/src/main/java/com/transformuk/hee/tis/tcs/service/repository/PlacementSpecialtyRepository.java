package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialtyPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PlacementSpecialtyRepository extends JpaRepository<PlacementSpecialty, PlacementSpecialtyPK> {

  @Query("SELECT ps FROM PlacementSpecialty ps WHERE ps.placement.id = :placementId")
  Set<PlacementSpecialty> findByPlacementId(@Param("placementId") Long placementId);

}
