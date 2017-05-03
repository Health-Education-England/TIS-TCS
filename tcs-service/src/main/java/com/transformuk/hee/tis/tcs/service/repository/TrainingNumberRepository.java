package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.domain.TrainingNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TrainingNumber entity.
 */
@SuppressWarnings("unused")
public interface TrainingNumberRepository extends JpaRepository<TrainingNumber, Long> {

}
