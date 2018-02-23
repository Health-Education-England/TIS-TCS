package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ProgrammeMembership entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeMembershipRepository extends JpaRepository<ProgrammeMembership, Long> {

  @Query("SELECT pm " +
      "FROM ProgrammeMembership pm " +
      "JOIN Programme p " +
      "ON pm.programmeId=p.id " +
      "WHERE personId = :traineeId " +
      "AND p.id = :programmeId")
  List<ProgrammeMembership> findByTraineeIdAndProgrammeId(@Param("traineeId") Long traineeId,
                                                              @Param("programmeId") Long programmeId);

}
