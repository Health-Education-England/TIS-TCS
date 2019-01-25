package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long>, JpaSpecificationExecutor<Specialty> {

  Page<Specialty> findBySpecialtyGroupIdIn(Long groupId, Pageable pageable);

  Set<Specialty> findBySpecialtyGroupIdIn(Long groupId);

  /**
   * Find a page of specialties related to a Programme via the Curricula
   *
   * @param programmeId The programme Id of the programme
   * @param pageable    The requested page of results
   * @return Page of the found Specialties for a Programme Id and Page
   */
  Page<Specialty> findSpecialtyDistinctByCurriculaProgrammesIdAndStatusIs(Long programmeId, Status status, Pageable pageable);

  /**
   * Find a page of specialties related to a Programme via the Curricula where the specialty name is like the provided
   * name
   *
   * @param programmeId The programme Id of the programme
   * @param name        The name of the specialty to search by
   * @param pageable    The requested page of results
   * @return Page of the found Specialties for a Programme Id and Page
   */
  Page<Specialty> findSpecialtyDistinctByCurriculaProgrammesIdAndNameContainingIgnoreCaseAndStatusIs(Long programmeId, String name, Status status, Pageable pageable);

  @Query("SELECT DISTINCT sp " +
      "FROM Specialty sp " +
      "JOIN sp.posts spp " +
      "JOIN spp.post p " +
      "JOIN p.programmes pr " +
      "JOIN p.placementHistory pl " +
      "JOIN pl.trainee t " +
      "WHERE pr.id = :programmeId " +
      "AND t.id = :personId " +
      "AND sp.status = :status")
  List<Specialty> findDistinctByProgrammeIdAndPersonIdAndStatus(@Param("programmeId") Long programmeId, @Param("personId") Long personId,
                                                                @Param("status") Status status);
}
