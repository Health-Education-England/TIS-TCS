package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the PersonalDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

}
