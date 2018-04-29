package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the RotationPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationPostRepository extends JpaRepository<RotationPost, Long> {
    
    List<RotationPost> findByPostId(Long id);
    
    Long deleteByPostId(Long postId);
}
