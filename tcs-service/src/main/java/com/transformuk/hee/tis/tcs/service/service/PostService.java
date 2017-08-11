package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Post.
 */
public interface PostService {

  /**
   * Save a post.
   *
   * @param postDTO the entity to save
   * @return the persisted entity
   */
  PostDTO save(PostDTO postDTO);

  /**
   * Save a list of post.
   *
   * @param postDTO the list of entities to save
   * @return the list of persisted entities
   */
  List<PostDTO> save(List<PostDTO> postDTO);

  /**
   * Update a single post
   *
   * @param postDTO the entity to update
   * @return the entity saved in DTO form
   */
  PostDTO update(PostDTO postDTO);

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PostDTO> findAll(Pageable pageable);

  /**
   * Get the "id" post.
   *
   * @param id the id of the entity
   * @return the entity
   */
  PostDTO findOne(Long id);

  /**
   * Delete the "id" post.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
