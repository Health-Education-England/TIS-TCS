package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

  private static final String COMMENT_NOT_FOUND =
      "The placement comment record for id '%d' could not be found.";

  private final PlacementCommentMapper placementCommentMapper;
  private final CommentRepository commentRepository;

  public CommentServiceImpl(PlacementCommentMapper placementCommentMapper,
      CommentRepository commentRepository) {
    this.placementCommentMapper = placementCommentMapper;
    this.commentRepository = commentRepository;
  }

  @Override
  public PlacementCommentDTO save(PlacementCommentDTO placementCommentDTO) {
    LOGGER.debug("Request to save Placement comment : {}", placementCommentDTO);

    Comment comment;
    if (placementCommentDTO.getId() != null) {
      long id = placementCommentDTO.getId();

      comment = commentRepository.findById(id).orElseThrow(() ->
          new IllegalArgumentException(String.format(COMMENT_NOT_FOUND, id))
      );

      placementCommentMapper.overwriteCommentEntityWithDTOComment(comment, placementCommentDTO);
    } else {
      comment = placementCommentMapper.toEntity(placementCommentDTO);
    }

    comment = commentRepository.saveAndFlush(comment);
    return placementCommentMapper.toDto(comment);
  }

  @Override
  public PlacementCommentDTO findByPlacementId(Long placementId) {
    LOGGER.debug("Request to retrieve Placement by placementId : {}", placementId);

    Optional<Comment> optionalComment = commentRepository
        .findFirstByPlacementIdOrderByAmendedDateDesc(placementId);
    return placementCommentMapper.toDto(optionalComment.orElse(new Comment()));
  }

  @Override
  public PlacementCommentDTO findById(Long id) {
    LOGGER.debug("Request to retrieve Placement by Id : {}", id);

    Comment comment = commentRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException(String.format(COMMENT_NOT_FOUND, id))
    );
    return placementCommentMapper.toDto(comment);
  }
}
