package com.transformuk.hee.tis.repository;

import com.transformuk.hee.tis.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post, Long> {

}
