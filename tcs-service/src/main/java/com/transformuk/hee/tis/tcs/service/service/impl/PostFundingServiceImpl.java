package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.PostFundingCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PostFundingSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.service.PostFundingService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostFundingMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing PostFunding.
 */
@Service
@Transactional
public class PostFundingServiceImpl implements PostFundingService {

  private final Logger log = LoggerFactory.getLogger(PostFundingServiceImpl.class);

  private final PostFundingRepository postFundingRepository;

  private final PostFundingMapper postFundingMapper;

  private final ApplicationEventPublisher applicationEventPublisher;

  public PostFundingServiceImpl(PostFundingRepository postFundingRepository,
      PostFundingMapper postFundingMapper, ApplicationEventPublisher applicationEventPublisher) {
    this.postFundingRepository = postFundingRepository;
    this.postFundingMapper = postFundingMapper;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   * Save a postFunding.
   *
   * @param postFundingDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PostFundingDTO save(PostFundingDTO postFundingDTO) {
    log.debug("Request to save PostFunding : {}", postFundingDTO);
    PostFunding postFunding = postFundingMapper.postFundingDTOToPostFunding(postFundingDTO);
    postFunding = postFundingRepository.save(postFunding);
    if (postFundingDTO.getId() == null) {
      applicationEventPublisher.publishEvent(new PostFundingCreatedEvent(postFundingDTO));
    } else {
      applicationEventPublisher.publishEvent(new PostFundingSavedEvent(postFundingDTO));
    }
    return postFundingMapper.postFundingToPostFundingDTO(postFunding);
  }

  /**
   * Save a list of postFunding.
   *
   * @param postFundingDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostFundingDTO> save(List<PostFundingDTO> postFundingDTO) {
    log.debug("Request to save PostFunding : {}", postFundingDTO);
    List<PostFunding> postFundings = postFundingMapper
        .postFundingDTOsToPostFundings(postFundingDTO);
    postFundings = postFundingRepository.saveAll(postFundings);
    postFundingDTO.stream().forEach(dto ->
        applicationEventPublisher.publishEvent(new PostFundingSavedEvent(dto))
    );
    return postFundingMapper.postFundingsToPostFundingDTOs(postFundings);
  }

  /**
   * Get all the postFundings.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostFundingDTO> findAll(Pageable pageable) {
    log.debug("Request to get all PostFundings");
    Page<PostFunding> result = postFundingRepository.findAll(pageable);
    return result.map(postFundingMapper::postFundingToPostFundingDTO);
  }

  /**
   * Get one postFunding by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PostFundingDTO findOne(Long id) {
    log.debug("Request to get PostFunding : {}", id);
    PostFunding postFunding = postFundingRepository.findById(id).orElse(null);
    return postFundingMapper.postFundingToPostFundingDTO(postFunding);
  }

  /**
   * Delete the  postFunding by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete PostFunding : {}", id);
    Optional<PostFunding> postFundingToDelete = postFundingRepository.findById(id);
    postFundingRepository.deleteById(id);
    if (postFundingToDelete.isPresent()) {
      applicationEventPublisher.publishEvent(
          new PostFundingDeletedEvent(postFundingToDelete.get()));
    }
  }

  /**
   * return the funding Status of the Post based on funding end date.
   *
   * @param postId the id of the associated post
   */
  @Override
  public Status getPostFundingStatusForPost(Long postId) {
    if (postFundingRepository.countCurrentFundings(postId)
        > 0) {
      return Status.CURRENT;
    }
    return Status.INACTIVE;
  }
}
