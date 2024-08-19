package com.transformuk.hee.tis.tcs.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PostMapperTest {

  PostMapper postMapper;

  @Before
  public void setup() {
    postMapper = new PostMapper();
  }

  @Test
  public void postToPostDtoTest() {
    //given
    String dateStamp = "2020-01-01T01:01:01.100";
    PostEsrEvent postEsrEvent = new PostEsrEvent();
    postEsrEvent.setEventDateTime(LocalDateTime.parse(dateStamp));
    postEsrEvent.setFilename("test.dat");
    postEsrEvent.setPositionNumber(1L);
    postEsrEvent.setStatus(PostEsrEventStatus.DELETED);
    postEsrEvent.setPositionId(2L);

    UUID fundingSubTypeId = UUID.randomUUID();
    PostFunding postFunding = new PostFunding();
    postFunding.setFundingSubTypeId(fundingSubTypeId);

    Post post = new Post();
    post.setId(3L);
    post.setPostEsrEvents(Collections.singleton(postEsrEvent));
    post.getFundings().add(postFunding);
    postEsrEvent.setPost(post);
    postFunding.setPost(post);

    //when
    PostDTO postDTO = postMapper.postToPostDTO(post);

    //then
    assertThat(postDTO.getCurrentReconciledEvents()).isNull();

    assertThat(postDTO.getFundings()).hasSize(1);
    PostFundingDTO postFundingDto = postDTO.getFundings().iterator().next();
    assertThat(postFundingDto.getFundingSubTypeId()).isEqualTo(fundingSubTypeId);
  }

  @Test
  public void postDtoToPostTest() {
    //given
    UUID fundingSubTypeId = UUID.randomUUID();
    PostFundingDTO postFundingDto = new PostFundingDTO();
    postFundingDto.setFundingSubTypeId(fundingSubTypeId);

    PostDTO postDto = new PostDTO();
    postDto.setId(3L);
    postDto.setFundings(Collections.singleton(postFundingDto));
    postFundingDto.setPostId(3L);

    //when
    Post post = postMapper.postDTOToPost(postDto);

    //then
    assertThat(post.getFundings()).hasSize(1);
    PostFunding postFunding = post.getFundings().iterator().next();
    assertThat(postFunding.getFundingSubTypeId()).isEqualTo(fundingSubTypeId);
  }
}
