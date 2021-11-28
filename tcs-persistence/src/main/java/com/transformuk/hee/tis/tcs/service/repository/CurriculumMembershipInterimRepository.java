package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.CurriculumMembershipInterim;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CurriculumMembershipInterim entity.
 */
@Repository
public interface CurriculumMembershipInterimRepository extends
    JpaRepository<CurriculumMembershipInterim, Long> {

  List<CurriculumMembershipInterim> findByIdIn(Set<Long> ids);
}
