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
     * @param rotationPostDTO the entity to save
     * @return the persisted entity
     */
    RotationPostDTO save(RotationPostDTO rotationPostDTO);

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
    RotationPostDTO findOne(Long id);
    
    /**
     * Get the "id" rotationPost.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RotationPostDTO findOneByPostId(Long id);

    /**
     * Delete the "id" rotationPost.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
