package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the PostFunding entity.
 */
@SuppressWarnings("unused")
public interface PostFundingRepository extends JpaRepository<PostFunding, Long> {

  @Query("SELECT count(id) FROM PostFunding "
      + "WHERE postId = :postId "
      + "AND (endDate >= current_date() OR"
      + "(endDate IS NULL AND startDate IS NOT NULL))")
  long countCurrentFundings(@Param("postId") Long postId);
}
