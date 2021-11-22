package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the ContactDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long> {

  NameProjection findContactDetailsById(Long id);

  List<ContactDetails> findContactDetailsByEmail(String email);
}
