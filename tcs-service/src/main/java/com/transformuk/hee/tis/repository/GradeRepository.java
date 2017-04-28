package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
public interface GradeRepository extends JpaRepository<Grade, Long> {

}
