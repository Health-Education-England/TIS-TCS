package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@SuppressWarnings("unused")
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

}
