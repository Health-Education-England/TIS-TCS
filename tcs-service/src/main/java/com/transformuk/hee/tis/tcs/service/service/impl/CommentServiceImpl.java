package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Autowired
	private PlacementCommentMapper placementCommentMapper;
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public PlacementCommentDTO save(PlacementCommentDTO placementCommentDTO) {
		log.debug("Request to save Placement comment : {}", placementCommentDTO);

		Comment comment;
		if(placementCommentDTO.getId() != null) {
      comment = commentRepository.findById(placementCommentDTO.getId()).orElse(null);
			placementCommentMapper.overwriteCommentEntityWithDTOComment(comment, placementCommentDTO);
		} else {
			comment = placementCommentMapper.toEntity(placementCommentDTO);
		}

		commentRepository.saveAndFlush(comment);
		return placementCommentMapper.toDto(comment);
	}

	@Override
	public PlacementCommentDTO findByPlacementId(Long placementId) {
		log.debug("Request to retrieve Placement by placementId : {}", placementId);

		Comment comment = commentRepository.findByPlacementId(placementId);
		return placementCommentMapper.toDto(comment);
	}

	@Override
	public PlacementCommentDTO findById(Long id) {
		log.debug("Request to retrieve Placement by Id : {}", id);

    Comment comment = commentRepository.findById(id).orElse(null);
		return placementCommentMapper.toDto(comment);
	}
}
