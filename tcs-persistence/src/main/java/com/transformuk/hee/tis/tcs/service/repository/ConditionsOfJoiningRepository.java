package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A JPA repository providing Conditions of Joining functionality.
 */
@Repository
public interface ConditionsOfJoiningRepository extends JpaRepository<ConditionsOfJoining, UUID> {

}
