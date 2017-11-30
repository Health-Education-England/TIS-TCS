package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonBasicDetailsRepository extends JpaRepository<PersonBasicDetails, Long>, JpaSpecificationExecutor<PersonBasicDetails> {

    List<PersonBasicDetails> findByIdIn(Set<Long> ids);
}
