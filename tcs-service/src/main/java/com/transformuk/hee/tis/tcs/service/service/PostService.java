package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Patch a list of post so that the links to old/new posts are saved. its important to note that
   * if a related post cannot be found, the existing post is cleared but if related post id is null
   * then it isnt cleared
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
   * Patch a list of post so that the links to fundings are saved.
   *
   * @param postDTO the entity to patch
   * @return the list of persisted entities
   */
  List<PostFundingDTO> patchPostFundings(PostDTO postDTO);

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
   * Get all the post by National Post Numbers
   *
   * @param npns
   * @return
   */
  List<PostDTO> findAllByNationalPostNumbers(List<String> npns);

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
  Page<PostViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilers,
      Pageable pageable);

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

  /**
   * Call Stored proc to build post view
   */
  CompletableFuture<Void> buildPostView();

  Page<PostEsrDTO> findPostsForEsrByDeaneryNumbers(List<String> deaneryNumbers, Pageable pageable);

  /**
   * Search for a page of Posts by national post number
   *
   * @param query
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable
   * @return
   */
  Page<PostViewDTO> findByNationalPostNumber(String query, List<ColumnFilter> columnFilers,
      Pageable pageable);

  /**
   * Method that will throw a not authorized exception if the current logged in user cannot view or
   * modify the person record
   *
   * @param personId the db managed id of the person record
   */
  void canLoggedInUserViewOrAmend(Long personId);

  /**
   * Find all Posts that are linked to a certain Programme and NPN
   *
   * @param programmeId The id of the Programme
   * @param npn         the npn of the post
   * @return List of PostDTO of the found Posts
   */
  List<PostDTO> findPostsForProgrammeIdAndNpn(Long programmeId, String npn);


  /**
   * Create new Post ESR Event and mark as Reconciled or Deleted.
   *
   * @param postId The id of the Post
   * @param postEsrEventDto         the post ESR event
   * @return Optional post ESR event
   */
  Optional<PostEsrEvent> markPostAsEsrReconciled(Long postId, PostEsrEventDto postEsrEventDto);
}
