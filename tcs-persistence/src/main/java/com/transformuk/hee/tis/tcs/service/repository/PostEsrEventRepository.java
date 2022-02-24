package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostEsrEvent entity.
 */
@Repository
public interface PostEsrEventRepository extends JpaRepository<PostEsrEvent, Long> {

  Set<PostEsrEvent> findPostEsrEventByPostIdIn(Iterable<Long> postIds);
}
