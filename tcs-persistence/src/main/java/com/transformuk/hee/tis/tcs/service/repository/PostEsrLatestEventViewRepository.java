package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostEsrEvent entity.
 */
@Repository
public interface PostEsrLatestEventViewRepository
    extends JpaRepository<PostEsrLatestEventView, Long> {

  Set<PostEsrLatestEventView> findPostEsrLatestEventByPostId(Long postId);
}
