package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the GmcDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GmcDetailsRepository extends JpaRepository<GmcDetails, Long> {

  IdProjection findByGmcNumber(String gmcNumber);

  List<GmcDetails> findByGmcNumberOrderById(String gmcNumber);

  @Query("SELECT g FROM GmcDetails g WHERE g.gmcNumber in :gmcIds")
  List<GmcDetails> findByGmcIdsIn(@Param("gmcIds") List<String> gmcIds);

}
