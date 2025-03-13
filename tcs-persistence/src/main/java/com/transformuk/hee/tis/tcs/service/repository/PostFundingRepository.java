package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PostFunding entity.
 */
@SuppressWarnings("unused")
public interface PostFundingRepository extends JpaRepository<PostFunding, Long> {
  List<PostFunding> findByPostId(Long postId);
}
