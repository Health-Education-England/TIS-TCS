package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Programme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Programme entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeRepository extends JpaRepository<Programme, Long>, JpaSpecificationExecutor<Programme> {

  Page<Programme> findByOwnerIn(Set<String> deaneries, Pageable pageable);

  List<Programme> findByProgrammeNumber(String programmeNumber);

  @Query(value = "select CASE WHEN count(p) > 0 THEN true ELSE false END from " +
      "Programme p join p.curricula c where p.id = :programmeId and c.id = :curriculumId ")
  boolean programmeCurriculumAssociationExists(@Param("programmeId") Long programmeId,
                                               @Param("curriculumId") Long curriculumId);
}
