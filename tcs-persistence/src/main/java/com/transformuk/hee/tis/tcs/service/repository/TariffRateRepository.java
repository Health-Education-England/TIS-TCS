package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.TariffRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TariffRate entity.
 */
@SuppressWarnings("unused")
public interface TariffRateRepository extends JpaRepository<TariffRate, Long> {

}
