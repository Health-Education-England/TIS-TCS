package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the GdcDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GdcDetailsRepository extends JpaRepository<GdcDetails, Long> {

  List<GdcDetails> findByGdcNumberIn(List<String> gdcIds);

  List<IdProjection> findByGdcNumber(String gdcNumber);
}
