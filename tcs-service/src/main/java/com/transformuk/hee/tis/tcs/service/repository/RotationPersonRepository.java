package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.RotationPerson;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the RotationPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationPersonRepository extends JpaRepository<RotationPerson, Long> {
	List<RotationPerson> findByPersonId(Long personId);
}
