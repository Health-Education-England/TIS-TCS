package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the PersonView entity.
 */
@SuppressWarnings("unused")
public interface PersonViewRepository extends JpaRepository<PersonView, Long>, JpaSpecificationExecutor<PersonView> {

}
