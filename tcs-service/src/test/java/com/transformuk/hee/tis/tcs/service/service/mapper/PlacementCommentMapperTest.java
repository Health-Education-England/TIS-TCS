package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import org.junit.Assert;
import org.junit.Test;

public class PlacementCommentMapperTest {

	public static final String TEST_COMMENT = "test comment";
	public static final Long PLACEMENT_ID = 36L;
	public static final Long COMMENT_ID = 1L;


	@Test
	public void mapsPlacementCommentToDTOSuccessfully() {
		PlacementCommentMapper placementCommentMapper = new PlacementCommentMapper();
		PlacementCommentDTO placementCommentDTO = placementCommentMapper.toDto(getComment());
		Assert.assertEquals(TEST_COMMENT, placementCommentDTO.getBody());
	}

	@Test
	public void mapsDTOToPlacementCommentSuccessfully() {
		PlacementCommentMapper placementCommentMapper = new PlacementCommentMapper();
		Comment comment = placementCommentMapper.toEntity(getPlacementCommentDTO());
		Assert.assertEquals(TEST_COMMENT, comment.getBody());
	}

	private PlacementCommentDTO getPlacementCommentDTO() {
		PlacementCommentDTO placementCommentDTO = new PlacementCommentDTO();
		PlacementDetailsDTO placement = new PlacementDetailsDTO();
		placement.setId(PLACEMENT_ID);
		placementCommentDTO.setBody(TEST_COMMENT);
		return placementCommentDTO;
	}

	private Comment getComment() {
		PlacementDetails placement = new PlacementDetails();
		placement.setId(PLACEMENT_ID);

		Comment comment = new Comment();
		comment.setBody(TEST_COMMENT);
		comment.setId(COMMENT_ID);
		comment.setPlacement(placement);
		return comment;
	}
}
