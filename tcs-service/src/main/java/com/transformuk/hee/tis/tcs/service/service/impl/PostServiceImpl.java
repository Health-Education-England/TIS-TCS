package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

  private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

  private final PostRepository postRepository;

  private final PostMapper postMapper;

  public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
    this.postRepository = postRepository;
    this.postMapper = postMapper;
  }

  /**
   * Save a post.
   *
   * @param postDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PostDTO save(PostDTO postDTO) {
    log.debug("Request to save Post : {}", postDTO);
    Post post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    PostDTO result = postMapper.postToPostDTO(post);
    return result;
  }

  /**
   * Save a list of post.
   *
   * @param postDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> save(List<PostDTO> postDTO) {
    log.debug("Request to save Post : {}", postDTO);
    List<Post> post = postMapper.postDTOsToPosts(postDTO);
    post = postRepository.save(post);
    List<PostDTO> result = postMapper.postsToPostDTOs(post);
    return result;
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Posts");
    Page<Post> result = postRepository.findAll(pageable);
    return result.map(post -> postMapper.postToPostDTO(post));
  }

  /**
   * Get one post by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PostDTO findOne(Long id) {
    log.debug("Request to get Post : {}", id);
    Post post = postRepository.findOne(id);
    PostDTO postDTO = postMapper.postToPostDTO(post);
    return postDTO;
  }

  /**
   * Delete the  post by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Post : {}", id);
    postRepository.delete(id);
  }
}
