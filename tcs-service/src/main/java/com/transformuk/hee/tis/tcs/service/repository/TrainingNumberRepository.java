package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the TrainingNumber entity.
 */
@SuppressWarnings("unused")
public interface TrainingNumberRepository extends JpaRepository<TrainingNumber, Long>, JpaSpecificationExecutor<TrainingNumber> {

    List<TrainingNumber> findByNumber(Integer number);

    List<TrainingNumber> findByProgrammeId(Long programme);

    List<TrainingNumber> findByIdIn(Set<Long> ids);
}
