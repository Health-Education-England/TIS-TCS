package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the RightToWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RightToWorkRepository extends JpaRepository<RightToWork, Long> {

}
