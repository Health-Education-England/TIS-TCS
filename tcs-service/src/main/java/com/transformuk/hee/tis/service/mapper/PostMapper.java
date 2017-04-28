package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.*;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Post and its DTO PostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostMapper {

	@Mapping(source = "oldPost.id", target = "oldPostId")
	@Mapping(source = "newPost.id", target = "newPostId")
    PostDTO postToPostDTO(Post post);

	List<PostDTO> postsToPostDTOs(List<Post> posts);

	@Mapping(source = "oldPostId", target = "oldPost")
	@Mapping(source = "newPostId", target = "newPost")
	Post postDTOToPost(PostDTO postDTO);

	List<Post> postDTOsToPosts(List<PostDTO> postDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default Post postFromId(Long id) {
		if (id == null) {
			return null;
		}
		Post post = new Post();
		post.setId(id);
		return post;
	}


}
