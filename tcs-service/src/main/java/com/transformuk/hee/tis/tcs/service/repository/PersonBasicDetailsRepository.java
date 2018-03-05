package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonBasicDetailsRepository extends JpaRepository<PersonBasicDetails, Long>, JpaSpecificationExecutor<PersonBasicDetails> {
    @Query("SELECT pbd.id from ContactDetails pbd WHERE pbd.id in :ids")
    List<PersonBasicDetails> findByIdIn(@Param("ids") Set<Long> ids);
}
