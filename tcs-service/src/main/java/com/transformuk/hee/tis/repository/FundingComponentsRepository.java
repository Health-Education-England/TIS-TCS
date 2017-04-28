package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.FundingComponents;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FundingComponents entity.
 */
@SuppressWarnings("unused")
public interface FundingComponentsRepository extends JpaRepository<FundingComponents, Long> {

}
