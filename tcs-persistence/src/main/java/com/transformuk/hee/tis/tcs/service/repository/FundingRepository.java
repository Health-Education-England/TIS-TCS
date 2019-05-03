package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Funding;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Funding entity.
 */
@SuppressWarnings("unused")
public interface FundingRepository extends JpaRepository<Funding, Long> {

}
