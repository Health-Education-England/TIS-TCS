package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * A JPA repository providing Conditions of Joining functionality.
 */
@Repository
public interface ConditionsOfJoiningRepository extends JpaRepository<ConditionsOfJoining, UUID> {

  @Query(value = "SELECT coj.* "
      + "FROM ConditionsOfJoining coj "
      + "JOIN ProgrammeMembership pm ON coj.programmeMembershipUuid = pm.uuid "
      + "WHERE pm.personId  = :traineeId "
      + "ORDER BY coj.programmeMembershipUuid", nativeQuery = true)
  List<ConditionsOfJoining> findByTraineeId(@Param("traineeId") Long traineeId);
}
