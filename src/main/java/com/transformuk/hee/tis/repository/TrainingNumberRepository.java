package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.TrainingNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TrainingNumber entity.
 */
@SuppressWarnings("unused")
public interface TrainingNumberRepository extends JpaRepository<TrainingNumber, Long> {

}
