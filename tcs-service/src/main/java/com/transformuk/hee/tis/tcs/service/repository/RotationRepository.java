package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Rotation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;
import java.util.Set;
import java.util.List;


/**
 * Spring Data JPA repository for the Rotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationRepository extends JpaRepository<Rotation, Long>, JpaSpecificationExecutor<Rotation> {

    Optional<Rotation> findByNameAndProgrammeId(String name, Long programmeId);

}
