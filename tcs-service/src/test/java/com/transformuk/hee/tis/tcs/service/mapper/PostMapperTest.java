package com.transformuk.hee.tis.tcs.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostMapperTest {

  PostMapper postMapper;

  @BeforeEach
  void setup() {
    postMapper = new PostMapper();
  }

  @Test
  void postToPostDtoTest() {
    //given
    UUID fundingSubTypeId = UUID.randomUUID();
    PostFunding postFunding = new PostFunding();
    postFunding.setFundingSubTypeId(fundingSubTypeId);

    Post post = new Post();
    post.setId(3L);
    post.getFundings().add(postFunding);
    postFunding.setPost(post);

    //when
    PostDTO postDTO = postMapper.postToPostDTO(post);

    //then
    assertNull(postDTO.getCurrentReconciledEvents());
    assertEquals(1, postDTO.getFundings().size());

    PostFundingDTO postFundingDto = postDTO.getFundings().iterator().next();
    assertEquals(fundingSubTypeId, postFundingDto.getFundingSubTypeId());
  }

  @Test
  void postDtoToPostTest() {
    //given
    UUID fundingSubTypeId = UUID.randomUUID();
    PostFundingDTO postFundingDto = new PostFundingDTO();
    postFundingDto.setFundingSubTypeId(fundingSubTypeId);

    PostDTO postDto = new PostDTO();
    postDto.setId(3L);
    postDto.setFundings(Collections.singleton(postFundingDto));
    postDto.setStatus(Status.CURRENT);
    postFundingDto.setPostId(3L);

    //when
    Post post = postMapper.postDTOToPost(postDto);

    //then
    assertEquals(Status.CURRENT, post.getFundingStatus());
    assertEquals(1, post.getFundings().size());
    PostFunding postFunding = post.getFundings().iterator().next();
    assertEquals(fundingSubTypeId, postFunding.getFundingSubTypeId());
  }

  @Test
  void shouldSetDefaultFundingStatusWhenPostDtoToPost() {
    PostDTO postDto = new PostDTO();
    postDto.setId(3L);

    //when
    Post post = postMapper.postDTOToPost(postDto);

    //then
    assertEquals(Status.INACTIVE, post.getFundingStatus());
  }
}
