package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlacementCommentMapper {

	public Comment toEntity(PlacementCommentDTO dto) {
		Comment comment = new Comment();
		comment.setId(dto.getId());
		comment.setBody(dto.getBody());
		return comment;
	}

	public PlacementCommentDTO toDto(Comment comment) {
		PlacementCommentDTO placementCommentDTO = new PlacementCommentDTO();
		placementCommentDTO.setId(comment.getId());
		placementCommentDTO.setBody(comment.getBody());
		return placementCommentDTO;
	}

	public Comment overwriteCommentEntityWithDTOCommentBody(Comment comment, PlacementCommentDTO commentDTO) {
		comment.setBody(commentDTO.getBody());
		return comment;
	}
}
