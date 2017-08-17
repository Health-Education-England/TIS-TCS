package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PostRelationshipsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

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
   * Update a list of post so that the links to old/new posts are saved. its important to note that if a related post
   * cannot be found, the existing post is cleared but if related post id is null then it isnt cleared
   *
   * @param postRelationshipsDTOS the list of entities to save
   * @return the list of persisted entities
   */
  List<PostDTO> updateOldNewPosts(List<PostRelationshipsDTO> postRelationshipsDTOS);

  /**
   * Update a single post
   *
   * @param postDTO the entity to update
   * @return the entity saved in DTO form
   */
  PostDTO update(PostDTO postDTO);

  /**
   * Get all the post by dbcs
   *
   * @param dbcs
   * @param pageable
   * @return
   */
  Page<PostDTO> findAll(Set<String> dbcs, Pageable pageable);

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PostDTO> findAll(Pageable pageable);

  /**
   * Get all the posts within the given designated body codes using the
   * given smart search string.
   *
   * @param dbcs         the designated body codes to search through not null
   * @param searchString the search string to match, can be null
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable     the pagination information
   * @return the list of entities
   */
  Page<PostDTO> advancedSearch(
      Set<String> dbcs, String searchString, List<ColumnFilter> columnFilers, Pageable pageable);


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
