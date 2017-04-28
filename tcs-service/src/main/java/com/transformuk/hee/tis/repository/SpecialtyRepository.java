package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@SuppressWarnings("unused")
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

}
