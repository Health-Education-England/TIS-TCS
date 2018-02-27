package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the GdcDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GdcDetailsRepository extends JpaRepository<GdcDetails, Long> {
	@Query("SELECT g.gdcNumber from GdcDetails g WHERE g.gdcNumber in :gdcIds")
	List<GdcDetails> findByGdcIdsIn(@Param("gdcIds") List<String> gdcIds);

  List<IdProjection> findByGdcNumber(String gdcNumber);
}
