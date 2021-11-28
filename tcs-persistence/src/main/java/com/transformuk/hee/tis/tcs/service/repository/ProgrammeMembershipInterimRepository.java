package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembershipInterim;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProgrammeMembershipInterim entity.
 */
@Repository
public interface ProgrammeMembershipInterimRepository extends
    JpaRepository<ProgrammeMembershipInterim, Long> {

  List<ProgrammeMembershipInterim> findByIdIn(Set<Long> ids);
}
