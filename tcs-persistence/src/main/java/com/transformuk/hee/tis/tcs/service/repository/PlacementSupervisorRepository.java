package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementSupervisorRepository extends
    JpaRepository<PlacementSupervisor, PlacementSupervisorId>,
    JpaSpecificationExecutor<PlacementSupervisor> {

  void deleteAllByIdPlacementId(long placementId);
}
