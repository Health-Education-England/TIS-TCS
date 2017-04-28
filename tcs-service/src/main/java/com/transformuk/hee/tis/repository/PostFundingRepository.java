package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.PostFunding;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PostFunding entity.
 */
@SuppressWarnings("unused")
public interface PostFundingRepository extends JpaRepository<PostFunding, Long> {

}
