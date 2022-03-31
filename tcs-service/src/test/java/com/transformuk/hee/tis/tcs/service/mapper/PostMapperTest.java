package com.transformuk.hee.tis.tcs.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
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
  public void postToPostDTOtest() {
    //given
    String dateStamp = "2020-01-01T01:01:01.100";
    PostEsrEvent postEsrEvent = new PostEsrEvent();
    postEsrEvent.setEventDateTime(LocalDateTime.parse(dateStamp));
    postEsrEvent.setFilename("test.dat");
    postEsrEvent.setPositionNumber(1L);
    postEsrEvent.setStatus(PostEsrEventStatus.DELETED);
    postEsrEvent.setPositionId(2L);
    Post post = new Post();
    post.setId(3L);
    post.setPostEsrEvents(Collections.singleton(postEsrEvent));
    postEsrEvent.setPost(post);

    //when
    PostDTO postDTO = postMapper.postToPostDTO(post);

    //then
    assertThat(postDTO.getPostEsrEvents()).hasSize(1);
    PostEsrEventDto postEsrEventDto = postDTO.getPostEsrEvents().iterator().next();
    assertThat(postEsrEventDto.getEventDateTime()).hasToString(dateStamp);
    assertThat(postEsrEventDto.getFilename()).isEqualTo("test.dat");
    assertThat(postEsrEventDto.getPositionNumber()).isEqualTo(1L);
    assertThat(postEsrEventDto.getStatus()).isEqualTo(PostEsrEventStatus.DELETED);
    assertThat(postEsrEventDto.getPositionId()).isEqualTo(2L);
  }
}
