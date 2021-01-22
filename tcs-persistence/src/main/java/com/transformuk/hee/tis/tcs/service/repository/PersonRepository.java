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
      "Select new com.transformuk.hee.tis.tcs.api.dto.ConnectionDto(cd.surname, cd.forenames, gmc.gmcNumber, gmc.id, prg.owner, prg.programmeName, pm.programmeMembershipType, pm.programmeStartDate, pm.programmeEndDate) "
          + "FROM Person p "
          + "JOIN ContactDetails cd on (cd.id = p.id) "
          + "JOIN GmcDetails gmc on (gmc.id = p.id) and gmc.gmcNumber <> 'UNKNOWN' "
          + "LEFT JOIN ProgrammeMembership pm on (pm.person = p.id) and curdate() between pm.programmeStartDate and pm.programmeEndDate "
          + "LEFT JOIN Programme prg on (prg.id = pm.programme) "
          + "LEFT JOIN Placement pl on (pl.trainee = p.id) and curdate() between pl.dateFrom and pl.dateTo "
          + "WHERE (pm.programmeMembershipType = 'MILITARY' OR pl.gradeId = 279 or gmc.gmcNumber in (:gmcIds)) "
          + "AND (:search is true or gmc.gmcNumber = :gmcNumber)")
  Page<ConnectionDto> getHiddenTraineeRecords(final Pageable pageable,
      @Param(value = "gmcIds") List<String> gmcIds, @Param(value = "search") boolean search,
      @Param(value = "gmcNumber") String gmcNumber);

  /**
   * Query for Revalidation Connection Summary page (All, Exception Tab).
   * Get trainees where (latest programmeMembership which is visitor OR expired) AND in the restricted local office.
   */
  final String getExceptionQuery =
      "Select distinct cd.surname, cd.forenames, gmc.gmcNumber, gmc.id, prg.owner, prg.programmeName, pm.programmeMembershipType, pm.programmeStartDate, pm.programmeEndDate "
      + "FROM Person p "
      + "JOIN ContactDetails cd on (cd.id = p.id) "
      + "JOIN GmcDetails gmc on (gmc.id = p.id) and gmc.gmcNumber <> 'UNKNOWN' "
      + "INNER JOIN (SELECT personId, MAX(programmeEndDate) as latestEndDate "
          + "FROM ProgrammeMembership "
          + "GROUP BY personId) latest "
      + "LEFT JOIN ProgrammeMembership pm on (pm.personId = p.id) and pm.personId = latest.personId and pm.programmeEndDate = latest.latestEndDate "
      + "LEFT JOIN Programme prg on (prg.id = pm.programmeId) "
      + "WHERE (curdate() > latest.latestEndDate or pm.programmeMembershipType = 'VISITOR' or gmc.gmcNumber in (:gmcIds)) "
      + "AND (prg.owner in (:owner)) "
      + "AND (:search is true or gmc.gmcNumber = :gmcNumber)";
  @Query(value = getExceptionQuery,
      countQuery = getExceptionQuery,
      nativeQuery = true)
  Page<Map<String,Object>> getExceptionTraineeRecords(final Pageable pageable,
      @Param(value = "gmcIds") List<String> gmcIds,
      @Param(value = "search") boolean search,
      @Param(value = "gmcNumber") String gmcNumber,
      @Param(value = "owner") Set<String> owner);
}
