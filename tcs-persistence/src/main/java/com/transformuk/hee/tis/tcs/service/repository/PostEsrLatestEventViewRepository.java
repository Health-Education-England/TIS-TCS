package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Spring Data JPA repository for the PostEsrEvent entity.
 */
@Repository
public interface PostEsrLatestEventViewRepository extends JpaRepository<PostEsrLatestEventView, Long> {

  Set<PostEsrLatestEventView> findPostEsrLatestEventByPostId(Long postId);
}
