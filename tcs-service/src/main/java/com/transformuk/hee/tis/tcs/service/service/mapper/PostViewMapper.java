package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.model.PostView;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PostView and its DTO PostViewDTO.
 * <p>
 * This mapper was created as mapstruct was having difficulty with some of the relationships within posts
 * It was having issues with the parent/child relationship between old/new post records causing stack overflows
 * and causing NPE's when trying to traverse through joins outside the JPA session.
 * <p>
 * This mapper gives more control over what details are converted
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostViewMapper {

  PostViewDTO postViewToPostViewDTO(PostView postView);

  List<PostViewDTO> postViewsToPostViewDTOs(List<PostView> postViews);

  PostView postViewDTOToPostView(PostViewDTO postViewDTO);

  List<PostView> postViewDTOsToPostViews(List<PostViewDTO> postViewDTOs);

}
