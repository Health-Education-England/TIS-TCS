package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity PostFunding and its DTO PostFundingDTO.
 */
@Mapper(componentModel = "spring", uses = {FundingMapper.class, FundingComponentsMapper.class,})
public interface PostFundingMapper {

  @Mapping(target="postId", source="postFunding.post.id")
  PostFundingDTO postFundingToPostFundingDTO(PostFunding postFunding);

  List<PostFundingDTO> postFundingsToPostFundingDTOs(List<PostFunding> postFundings);

  @Mapping(target="post.id", source="postFundingDTO.postId")
  PostFunding postFundingDTOToPostFunding(PostFundingDTO postFundingDTO);

  List<PostFunding> postFundingDTOsToPostFundings(List<PostFundingDTO> postFundingDTOs);

  /**
   * generating the fromId for all mappers if the databaseType is sql, as the class has relationship
   * to it might need it, instead of creating a new attribute to know if the entity has any
   * relationship from some other entity
   *
   * @param id id of the entity
   * @return the entity instance
   */

  default PostFunding postFundingFromId(Long id) {
    if (id == null) {
      return null;
    }
    PostFunding postFunding = new PostFunding();
    postFunding.setId(id);
    return postFunding;
  }


}
