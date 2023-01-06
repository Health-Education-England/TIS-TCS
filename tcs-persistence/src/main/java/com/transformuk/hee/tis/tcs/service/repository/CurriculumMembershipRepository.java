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

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "JOIN FETCH cm.programmeMembership pm "
      + "WHERE pm.person.id = :traineeId "
      + "AND pm.programme.id = :programmeId")
  List<CurriculumMembership> findByTraineeIdAndProgrammeId(@Param("traineeId") Long traineeId,
                                                          @Param("programmeId") Long programmeId);

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "JOIN FETCH cm.programmeMembership pm "
      + "WHERE pm.person.id  = :traineeId")
  List<CurriculumMembership> findByTraineeId(@Param("traineeId") Long traineeId);

  //Find latest curriculum membership of a trainee
  @Query(value = "SELECT cm.* "
      + "FROM CurriculumMembership cm "
      + "JOIN ProgrammeMembership pm ON cm.programmeMembershipUuid = pm.uuid "
      + "WHERE pm.personId  = :traineeId "
      + "ORDER BY pm.programmeEndDate DESC LIMIT 1", nativeQuery = true)
  CurriculumMembership findLatestCurriculumMembershipByTraineeId(@Param("traineeId")
      Long traineeId);

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "JOIN FETCH cm.programmeMembership pm "
      + "WHERE pm.person.id = :traineeId "
      + "ORDER BY pm.programmeEndDate DESC")
  List<CurriculumMembership> findAllCurriculumMembershipInDescOrderByTraineeId(
      @Param("traineeId") Long traineeId);

  @Query(value = "SELECT cm "
      + "FROM CurriculumMembership cm "
      + "JOIN FETCH cm.programmeMembership pm "
      + "WHERE pm.programme.id = :programmeId")
  List<CurriculumMembership> findByProgrammeId(@Param("programmeId") Long programmeId);

  List<CurriculumMembership> findByIdIn(Set<Long> ids);

  //Find latest curriculum membership of a trainee order by curriculum end date
  @Query(value = "SELECT cm.* "
      + "FROM CurriculumMembership cm "
      + "JOIN ProgrammeMembership pm ON cm.programmeMembershipUuid = pm.uuid "
      + "WHERE pm.personId = :traineeId "
      + "ORDER BY cm.curriculumEndDate DESC LIMIT 1", nativeQuery = true)
  CurriculumMembership findLatestCurriculumByTraineeId(@Param("traineeId") Long traineeId);
}
