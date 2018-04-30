package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import java.util.List;

/**
 * Service Interface for managing RotationPost.
 */
public interface RotationPostService {

    /**
     * Save a rotationPost.
     *
     * @param rotationPostDTOs the entity to save
     * @return the persisted entity
     */
    List<RotationPostDTO> saveAll(List<RotationPostDTO> rotationPostDTOs);
    
    /**
     * Get all the rotationPosts.
     *
     * @return the list of entities
     */
    List<RotationPostDTO> findAll();
    
    /**
     * Get the "id" rotationPost.
     *
     * @param id the id of the entity
     * @return the entity
     */
    List<RotationPostDTO> findByPostId(Long id);
    
    void delete(Long postId);
}
