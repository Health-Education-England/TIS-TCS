package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PlacementView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Placement entity.
 */
@SuppressWarnings("unused")
public interface PlacementViewRepository extends JpaRepository<PlacementView, Long> {

  List<PlacementView> findAllByTraineeIdOrderByDateToDesc(Long traineeId);

  List<PlacementView> findAllByPostIdOrderByDateToDesc(Long traineeId);

}
