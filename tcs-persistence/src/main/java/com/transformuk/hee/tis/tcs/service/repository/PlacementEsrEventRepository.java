package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementEsrEventRepository extends JpaRepository<PlacementEsrEvent, Long> {

  Set<PlacementEsrEvent> findPlacementEsrEventByPlacementIdIn(Iterable<Long> placementIds);
}
