package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  @EntityGraph(value = "all.oldNewPost.programmes.sites.grades.specialties.placementHistory", type= EntityGraph.EntityGraphType.FETCH)
  @Override
  Page<Post> findAll(Pageable pageable);

  @EntityGraph(value = "all.oldNewPost.programmes.sites.grades.specialties.placementHistory", type= EntityGraph.EntityGraphType.FETCH)
  @Override
  Page<Post> findAll(Specification<Post> specification, Pageable pageable);

  Page<Post> findByManagingLocalOfficeIn(Set<String> deaneries, Pageable pageable);
}
