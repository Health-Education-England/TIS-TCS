package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the CurriculumMembership entity.
 */
@SuppressWarnings("unused")
public interface CurriculumMembershipRepository extends JpaRepository<CurriculumMembership, Long> {
  //Note: personId, programme, programmeEndDate etc. are now only updated in the
  //programmeMembership table, and may be null in the curriculumMembership table.

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "WHERE cm.programmeMembership.person.id = :traineeId "
      + "AND cm.programmeMembership.programme.id = :programmeId")
  List<CurriculumMembership> findByTraineeIdAndProgrammeId(@Param("traineeId") Long traineeId,
                                                          @Param("programmeId") Long programmeId);

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "WHERE cm.programmeMembership.person.id = :traineeId")
  List<CurriculumMembership> findByTraineeId(@Param("traineeId") Long traineeId);

  //Find latest curriculum membership of a trainee
  @Query(value = "SELECT cm.* FROM CurriculumMembership cm "
      + "JOIN ProgrammeMembership pm ON cm.programmeMembershipId = pm.id "
      + "WHERE pm.personId = :traineeId ORDER BY pm.programmeEndDate DESC LIMIT 1",
      nativeQuery = true)
  CurriculumMembership findLatestCurriculumMembershipByTraineeId(@Param("traineeId")
                                                                     Long traineeId);

  @Query(value = "SELECT cm.* FROM CurriculumMembership cm "
      + "JOIN ProgrammeMembership pm ON cm.programmeMembershipId = pm.id "
      + "WHERE pm.personId = :traineeId "
      + "ORDER BY pm.programmeEndDate DESC", nativeQuery = true)
  List<CurriculumMembership> findAllCurriculumMembershipInDescOrderByTraineeId(
      @Param("traineeId") Long traineeId);

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "WHERE cm.programmeMembership.programme.id = :programmeId")
  List<CurriculumMembership> findByProgrammeId(@Param("programmeId") Long programmeId);

  List<CurriculumMembership> findByIdIn(Set<Long> ids);

  //Find latest curriculum membership of a trainee order by curriculum end date
  @Query(value = "SELECT cm.* FROM CurriculumMembership cm "
      + "JOIN ProgrammeMembership pm ON cm.programmeMembershipId = pm.id "
      + "WHERE pm.personId = :traineeId ORDER BY cm.curriculumEndDate DESC LIMIT 1",
      nativeQuery = true)
  CurriculumMembership findLatestCurriculumByTraineeId(@Param("traineeId") Long traineeId);
}
