package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtySimple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@SuppressWarnings("unused")
public interface SpecialtySimpleRepository extends JpaRepository<SpecialtySimple, Long> {

  List<SpecialtySimple> findByIdIn(List<Long> ids);

}
