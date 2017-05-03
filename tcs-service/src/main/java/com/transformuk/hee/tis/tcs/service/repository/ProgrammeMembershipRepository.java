package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ProgrammeMembership entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeMembershipRepository extends JpaRepository<ProgrammeMembership, Long> {

}
