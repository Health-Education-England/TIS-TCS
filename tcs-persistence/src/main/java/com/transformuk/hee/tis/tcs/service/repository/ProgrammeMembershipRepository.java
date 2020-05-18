package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the ProgrammeMembership entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeMembershipRepository extends JpaRepository<ProgrammeMembership, Long> {

  @Query(value = "SELECT pm "
      + "FROM ProgrammeMembership pm "
      + "WHERE personId = :traineeId "
      + "AND pm.programme.id = :programmeId")
  List<ProgrammeMembership> findByTraineeIdAndProgrammeId(@Param("traineeId") Long traineeId,
      @Param("programmeId") Long programmeId);

  @Query(value = "SELECT pm "
      + "FROM ProgrammeMembership pm "
      + "WHERE personId = :traineeId")
  List<ProgrammeMembership> findByTraineeId(@Param("traineeId") Long traineeId);

  //Find latest programme membership of a trainee
  @Query(value = "SELECT pm.* FROM ProgrammeMembership pm "
      + "WHERE pm.personId = :traineeId ORDER BY pm.programmeEndDate DESC LIMIT 1", nativeQuery = true)
  ProgrammeMembership findLatestProgrammeMembershipByTraineeId(@Param("traineeId") Long traineeId);
}
