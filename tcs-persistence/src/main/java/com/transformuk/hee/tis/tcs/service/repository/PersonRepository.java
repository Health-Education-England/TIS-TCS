package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionDto;
import com.transformuk.hee.tis.tcs.service.model.Person;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long>,
    JpaSpecificationExecutor<Person>, CustomPersonRepository {

  @Procedure(name = "build_person_localoffice")
  void buildPersonView();

  Long findOneIdByGmcDetailsGmcNumber(String gmcNumber);

  List<Person> findByPublicHealthNumber(String publicHealthNumber);

  List<Person> findByPublicHealthNumberIn(List<String> publicHealthNumbers);

  @Query("SELECT p " +
      "FROM Person p " +
      "LEFT JOIN FETCH p.associatedTrusts " +
      "WHERE p.id = :id")
  Optional<Person> findPersonById(@Param(value = "id") Long id);

  @Query(
      "Select new com.transformuk.hee.tis.tcs.api.dto.ConnectionDto(cd.surname, cd.forenames, " +
          "gmc.gmcNumber, gmc.id, prg.owner, prg.programmeName, cm.programmeMembershipType, " +
          "cm.programmeStartDate, cm.programmeEndDate) "
          + "FROM Person p "
          + "JOIN ContactDetails cd on (cd.id = p.id) "
          + "JOIN GmcDetails gmc on (gmc.id = p.id) and gmc.gmcNumber <> 'UNKNOWN' "
          + "LEFT JOIN CurriculumMembership cm on (cm.person = p.id) " +
          "and curdate() between cm.programmeStartDate and cm.programmeEndDate "
          + "LEFT JOIN Programme prg on (prg.id = cm.programme) "
          + "LEFT JOIN Placement pl on (pl.trainee = p.id) and curdate() between pl.dateFrom and pl.dateTo "
          + "WHERE (cm.programmeMembershipType = 'MILITARY' OR pl.gradeId = 279 or gmc.gmcNumber in (:gmcIds)) "
          + "AND (:search is true or gmc.gmcNumber = :gmcNumber)")
  Page<ConnectionDto> getHiddenTraineeRecords(final Pageable pageable,
      @Param(value = "gmcIds") List<String> gmcIds, @Param(value = "search") boolean search,
      @Param(value = "gmcNumber") String gmcNumber);

  /**
   * Query for Revalidation Connection Summary page (Exception Tab).
   * Get trainees where (it is in the search gmcNumber)
   *                    AND
   *                      (it is in the gmcId list for reval exception queue)
   *                      OR
   *                      (latest programmeMembership which is visitor OR expired)
   *                      AND in the restricted local office.
   */
  final String GET_EXCEPTION_QUERY =
      "Select distinct cd.surname, cd.forenames, gmc.gmcNumber, gmc.id, prg.owner, "
          + "prg.programmeName, latestCm.programmeMembershipType, latestCm.programmeStartDate, "
          + "latestCm.programmeEndDate "
      + "FROM Person p "
      + "INNER JOIN ContactDetails cd on (cd.id = p.id) "
      + "INNER JOIN GmcDetails gmc on (gmc.id = p.id) and gmc.gmcNumber <> 'UNKNOWN' "
      + "LEFT JOIN (SELECT cmi.personId, cmi.programmeStartDate, cmi.programmeEndDate, "
          + "cmi.programmeId, cmi.programmeMembershipType "
          + "FROM CurriculumMembership cmi "
          // this inner join is to get the latest curriculumMembership of each trainee
          + "INNER JOIN (SELECT personId, MAX(programmeEndDate) AS latestEndDate "
              + "FROM CurriculumMembership "
              + "GROUP BY personId) latest ON cmi.personId = latest.personId "
              + "AND cmi.programmeEndDate = latest.latestEndDate "
          + ") latestCm ON (latestCm.personId = p.id) "
      + "LEFT JOIN Programme prg on (prg.id = latestCm.programmeId) "
      + "WHERE ( "
              // get trainees from gmcId list even if some of them have no programmeMembership
              + "( latestCm.personId IS NULL AND gmc.gmcNumber IN (:gmcIds) ) "
          + "OR ( "
              + "( curdate() > latestCm.programmeEndDate "
                  + "OR latestCm.programmeMembershipType = 'VISITOR' "
                  + "OR gmc.gmcNumber in (:gmcIds) ) "
              + "AND prg.OWNER in (:owner) ) "
      + ") AND (:search is true or gmc.gmcNumber = :gmcNumber)";
  @Query(value = GET_EXCEPTION_QUERY,
      countQuery = GET_EXCEPTION_QUERY,
      nativeQuery = true)
  Page<Map<String,Object>> getExceptionTraineeRecords(final Pageable pageable,
      @Param(value = "gmcIds") List<String> gmcIds,
      @Param(value = "search") boolean search,
      @Param(value = "gmcNumber") String gmcNumber,
      @Param(value = "owner") Set<String> owner);
}
