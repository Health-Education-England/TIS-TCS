package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post, Long> {

//  @Query(
//      "SELECT p " +
//          "FROM Post p " +
//          "JOIN FETCH p.specialties sp " +
//          "WHERE p.id = :postId"
//  )
//  Post findFullPostDetailsByPostId(@Param("postId") Long postId);
}
