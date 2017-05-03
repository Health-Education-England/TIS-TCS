package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.domain.PostFunding;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PostFunding entity.
 */
@SuppressWarnings("unused")
public interface PostFundingRepository extends JpaRepository<PostFunding, Long> {

}
