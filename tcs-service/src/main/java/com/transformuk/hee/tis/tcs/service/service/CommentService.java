package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;

public interface CommentService {

  PlacementCommentDTO save(PlacementCommentDTO placementCommentDTO);

  PlacementCommentDTO findByPlacementId(Long placementId);

  PlacementCommentDTO findById(Long id);

}
