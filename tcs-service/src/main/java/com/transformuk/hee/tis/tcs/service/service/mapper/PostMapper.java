package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Post and its DTO PostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostMapper {

  PostDTO postToPostDTO(Post post);

  List<PostDTO> postsToPostDTOs(List<Post> posts);

  Post postDTOToPost(PostDTO postDTO);

  List<Post> postDTOsToPosts(List<PostDTO> postDTOs);

}
