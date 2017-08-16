package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.cbEqual;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

  private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
  public static final String SPECIALTIES = "specialties";
  public static final String GRADES = "grades";
  public static final String SITES = "sites";

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostGradeRepository postGradeRepository;
  @Autowired
  private PostSiteRepository postSiteRepository;
  @Autowired
  private PostSpecialtyRepository postSpecialtyRepository;
  @Autowired
  private PostMapper postMapper;

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
    return postMapper.postToPostDTO(post);
  }

  /**
   * Save a list of post.
   *
   * @param postDTOs the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> save(List<PostDTO> postDTOs) {
    log.debug("Request to save Post : {}", postDTOs);
    List<Post> post = postMapper.postDTOsToPosts(postDTOs);
    post = postRepository.save(post);
    return postMapper.postsToPostDTOs(post);
  }


  @Override
  public PostDTO update(PostDTO postDTO) {
    Post post = postRepository.findOne(postDTO.getId());

    //clear all the relations
    postGradeRepository.delete(post.getGrades());
    postSiteRepository.delete(post.getSites());
    postSpecialtyRepository.delete(post.getSpecialties());

    post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostDTO> findAll(Set<String> dbcs, Pageable pageable) {
    log.debug("Request to get all Posts");

    Set<String> deaneries = DesignatedBodyMapper.map(dbcs);
    Page<Post> result = postRepository.findByManagingLocalOfficeIn(deaneries, pageable);
    return result.map(post -> postMapper.postToPostDTO(post));
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

  @Override
  @Transactional(readOnly = true)
  public Page<PostDTO> advancedSearch(
      Set<String> dbcs, String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    Set<String> localOffices = DesignatedBodyMapper.map(dbcs);
    List<Specification<Post>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("nationalPostNumber", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {
        specs.add(in(cf.getName(), cf.getValues()));
        if (cf.getName().contains(SPECIALTIES)) {
          specs.add(cbEqual(SPECIALTIES, "postSpecialtyType", PostSpecialtyType.PRIMARY));
        }
        if (cf.getName().contains(GRADES)) {
          specs.add(cbEqual(GRADES, "postGradeType", PostGradeType.APPROVED));
        }
        if (cf.getName().contains(SITES)) {
          specs.add(cbEqual(SITES, "postSiteType", PostSiteType.PRIMARY));
        }
      });
    }
    //finally filter by deaneries
    specs.add(in("managingLocalOffice", localOffices.stream().collect(Collectors.toList())));
    Specifications<Post> fullSpec = Specifications.where(specs.get(0));
    //add the rest of the specs that made it in
    for (int i = 1; i < specs.size(); i++) {
      fullSpec = fullSpec.and(specs.get(i));
    }
    Page<Post> result = postRepository.findAll(fullSpec, pageable);

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
    return postMapper.postToPostDTO(post);
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
