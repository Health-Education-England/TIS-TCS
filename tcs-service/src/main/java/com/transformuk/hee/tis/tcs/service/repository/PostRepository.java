package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  @Procedure("build_post_view")
  void buildPostView();

  Post findPostByIntrepidId(String intrepidId);

  Set<Post> findPostByIntrepidIdIn(Set<String> intrepidIds);

  Page<Post> findByOwnerIn(Set<String> deaneries, Pageable pageable);

  List<Post> findByNationalPostNumber(String nationalPostNumber);

  List<Post> findByNationalPostNumberIn(List<String> nationalPostNumbers);

  OwnerProjection findPostById(Long id);

  Set<Post> findByNationalPostNumberStartingWith(String nationalPostNumberNoCounter);

  Page<EsrPostProjection> findByIdNotNullAndNationalPostNumberIn(List<String> nationalPostNumbers, Pageable pageable);

  @Query("SELECT p " +
      "FROM Post p " +
      "LEFT JOIN FETCH p.associatedTrusts " +
      "WHERE p.id = :id")
  Optional<Post> findPostWithTrustsById(@Param("id") Long id);
}
