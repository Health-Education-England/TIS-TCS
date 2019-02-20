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

import java.util.Optional;
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

  /**
   * Get a Page of Specialties linked to a Post whereby the Post is linked to a Programme Id
   *
   * @param programmeId The Programme Id
   * @param status      The status which the Specialty needs to be in
   * @param pageable    Pageable containing sort and paging information
   * @return A Page of Specialties found
   */
  @Query("SELECT DISTINCT sp " +
      "FROM Specialty sp " +
      "JOIN sp.posts psp " +
      "JOIN psp.post p " +
      "JOIN p.programmes pr " +
      "WHERE pr.id = :programmeId " +
      "AND sp.status = :status ")
  Page<Specialty> findSpecialtiesByProgrammeId(@Param("programmeId") Long programmeId, @Param("status") Status status, Pageable pageable);

  /**
   * Get a Page of Specialties linked to a Post whereby the Post is linked to a Programme Id and the Specialty name is like the provided query
   *
   * @param programmeId The Programme Id
   * @param query       The name of the Specialty to look for
   * @param status      The status of the Specialty needs to be in
   * @param pageable    Pageable containing sort and paging information
   * @return A Page of Specialties found
   */
  @Query("SELECT DISTINCT sp " +
      "FROM Specialty sp " +
      "JOIN sp.posts psp " +
      "JOIN psp.post p " +
      "JOIN p.programmes pr " +
      "WHERE pr.id = :programmeId " +
      "AND sp.status = :status " +
      "AND sp.name LIKE %:query%")
  Page<Specialty> findSpecialtiesByProgrammeIdAndName(@Param("programmeId") Long programmeId, @Param("query") String query,
                                                      @Param("status") Status status, Pageable pageable);

  /**
   * Get a Set of Specialties that a trainee is on
   *
   * @param programmeId
   * @param personId
   * @param status
   * @return
   */
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
  Set<Specialty> findDistinctByProgrammeIdAndPersonIdAndStatus(@Param("programmeId") Long programmeId, @Param("personId") Long personId,
                                                               @Param("status") Status status);

  @Query("SELECT s " +
      "FROM Specialty s " +
      "JOIN FETCH s.specialtyTypes st " +
      "WHERE s.id = :id ")
  Optional<Specialty> findSpecialtyByIdEagerFetch(@Param("id") Long id);
}
