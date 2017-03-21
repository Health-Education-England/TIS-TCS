package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Programme;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Programme entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeRepository extends JpaRepository<Programme,Long> {

}
