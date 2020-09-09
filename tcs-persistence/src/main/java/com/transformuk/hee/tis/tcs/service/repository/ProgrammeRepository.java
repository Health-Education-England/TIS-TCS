package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Programme;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Spring Data JPA repository for the Programme entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeRepository extends JpaRepository<Programme, Long>,
    JpaSpecificationExecutor<Programme> {

  @PostFilter("hasPermission(filterObject, 'READ')")
  List<Programme> findByIdIn(Set<Long> ids);

  Page<Programme> findByOwnerIn(Set<String> deaneries, Pageable pageable);

  List<Programme> findByProgrammeNumber(String programmeNumber);

  @Query(value = "select CASE WHEN count(p) > 0 THEN true ELSE false END from " +
      "Programme p join p.curricula pc join pc.curriculum c where p.id = :programmeId and c.id = :curriculumId ")
  boolean programmeCurriculumAssociationExists(@Param("programmeId") Long programmeId,
      @Param("curriculumId") Long curriculumId);

  @PostFilter("hasPermission(filterObject, 'READ')")
  @Query(value =
      "SELECT distinct pm.programme " +
          "FROM ProgrammeMembership pm " +
          "WHERE pm.person.id = :personId")
  List<Programme> findByProgrammeMembershipPersonId(@Param("personId") Long personId);

  @Query("SELECT p " +
      "FROM Programme p " +
      "LEFT JOIN FETCH p.curricula pc " +
      "LEFT JOIN FETCH pc.curriculum c " +
      "LEFT JOIN FETCH c.specialty s " +
      "LEFT JOIN FETCH s.specialtyTypes st " +
      "WHERE p.id = :id ")
  Optional<Programme> findProgrammeByIdEagerFetch(@Param("id") Long id);
}
