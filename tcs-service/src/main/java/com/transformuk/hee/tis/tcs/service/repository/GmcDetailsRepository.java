package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the GmcDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GmcDetailsRepository extends JpaRepository<GmcDetails, Long> {

}
