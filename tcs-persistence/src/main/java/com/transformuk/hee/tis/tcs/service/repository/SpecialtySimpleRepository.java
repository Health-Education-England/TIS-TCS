package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.SpecialtySimple;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@SuppressWarnings("unused")
public interface SpecialtySimpleRepository extends JpaRepository<SpecialtySimple, Long> {

  List<SpecialtySimple> findByIdIn(List<Long> ids);

}
