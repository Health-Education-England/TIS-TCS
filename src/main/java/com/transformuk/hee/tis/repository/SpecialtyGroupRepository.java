package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.SpecialtyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the SpecialtyGroup entity.
 */
@SuppressWarnings("unused")
public interface SpecialtyGroupRepository extends JpaRepository<SpecialtyGroup, Long> {

}
