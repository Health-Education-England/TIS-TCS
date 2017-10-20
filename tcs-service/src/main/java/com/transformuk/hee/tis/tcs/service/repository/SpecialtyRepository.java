package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Specialty entity.
 */

public interface SpecialtyRepository extends JpaRepository<Specialty, Long>, JpaSpecificationExecutor<Specialty> {

  Page<Specialty> findBySpecialtyGroupIdIn(Long groupId, Pageable pageable);

}
