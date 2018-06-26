package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;

import org.springframework.stereotype.Component;

@Component
public class PlacementCommentMapper {

	public Comment toEntity(PlacementCommentDTO dto) {
		Comment comment = new Comment();
		comment.setId(dto.getId());
		comment.setBody(dto.getBody());
		comment.setAuthor(dto.getAuthor());
		comment.setSource(dto.getSource());
		return comment;
	}

	public PlacementCommentDTO toDto(Comment comment) {
		PlacementCommentDTO placementCommentDTO = new PlacementCommentDTO();
		placementCommentDTO.setId(comment.getId());
		placementCommentDTO.setBody(comment.getBody());
		placementCommentDTO.setAuthor(comment.getAuthor());
		placementCommentDTO.setSource(comment.getSource());
		return placementCommentDTO;
	}

	public Comment overwriteCommentEntityWithDTOComment(Comment comment, PlacementCommentDTO commentDTO) {
		comment.setBody(commentDTO.getBody());
		comment.setAuthor(commentDTO.getAuthor());
		comment.setSource(commentDTO.getSource());
		return comment;
	}
}
