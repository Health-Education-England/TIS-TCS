package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Rotation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Rotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationRepository extends JpaRepository<Rotation, Long>,
    JpaSpecificationExecutor<Rotation> {

  Optional<Rotation> findByIdAndProgrammeId(Long id, Long programmeId);

  @Query(value = "select r from Rotation r where lower(r.name) = lower(?1) and r.programmeId = ?2 "
      + "and r.status = 'Current'")
  List<Rotation> findCurrentByNameAndProgrammeId(String name, Long programmeId);
}
