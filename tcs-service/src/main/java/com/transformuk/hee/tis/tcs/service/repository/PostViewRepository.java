package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the PostView entity.
 */
@SuppressWarnings("unused")
public interface PostViewRepository extends JpaRepository<PostView, Long>, JpaSpecificationExecutor<PostView> {

}
