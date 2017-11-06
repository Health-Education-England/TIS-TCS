package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
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
   * Patch a list of post so that the links to old/new posts are saved. its important to note that if a related post
   * cannot be found, the existing post is cleared but if related post id is null then it isnt cleared
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  List<PostDTO> patchOldNewPosts(List<PostDTO> postDTOList);

  /**
   * Patch a list of post so that the links to sites are saved.
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  List<PostDTO> patchPostSites(List<PostDTO> postDTOList);

  /**
   * Patch a list of post so that the links to grades are saved.
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  List<PostDTO> patchPostGrades(List<PostDTO> postDTOList);

  /**
   * Patch a list of post so that the links to grades are saved.
   *
   * @param postDTOList the list of entities to patch
   * @return the list of persisted entities
   */
  List<PostDTO> patchPostProgrammes(List<PostDTO> postDTOList);

  /**
   * Patch a list of post so that the links to specialties are saved.
   *
   * @param postDTOList the list of entities to patch
   * @return the list of persisted entities
   */
  List<PostDTO> patchPostSpecialties(List<PostDTO> postDTOList);

  /**
   * Patch a list of post so that the links to placements are saved.
   *
   * @param postDTOList the list of entities to patch
   * @return the list of persisted entities
   */
  List<PostDTO> patchPostPlacements(List<PostDTO> postDTOList);


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
  Page<PostViewDTO> findAll(Pageable pageable);

  /**
   * Get all the posts using the given smart search string and filters.
   *
   * @param searchString the search string to match, can be null
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable     the pagination information
   * @return the list of entities
   */
  Page<PostViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilers, Pageable pageable);


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
