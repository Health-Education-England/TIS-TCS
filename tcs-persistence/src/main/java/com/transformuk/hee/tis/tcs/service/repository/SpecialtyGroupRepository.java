package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the SpecialtyGroup entity.
 */
@SuppressWarnings("unused")
public interface SpecialtyGroupRepository extends JpaRepository<SpecialtyGroup, Long>,
    JpaSpecificationExecutor<SpecialtyGroup> {

}
