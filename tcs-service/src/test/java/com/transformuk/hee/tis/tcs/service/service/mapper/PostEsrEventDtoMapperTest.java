package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import com.transformuk.hee.tis.tcs.service.model.PostEsrLatestEventView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PostEsrEventDtoMapperTest {

  private PostEsrEventDtoMapper mapper;
  private PostEsrLatestEventView postEsrLatestEventView;

  private static final Long ID = 1L;
  private static final Long POST_ID = 1L;
  private static final Long POSITION_ID = 1L;
  private static final Long POSITION_NUMBER = 1L;
  private static final String FILE_NAME = "fileName";
  private static final PostEsrEventStatus STATUS = PostEsrEventStatus.RECONCILED;
  private static final LocalDateTime eventTime = LocalDateTime.of(2024, Month.AUGUST, 19, 8, 20);

  @BeforeEach
  void setUp() {
    mapper = new PostEsrEventDtoMapperImpl();
    postEsrLatestEventView = new PostEsrLatestEventView();
    postEsrLatestEventView.setId(ID);
    postEsrLatestEventView.setPostId(POST_ID);
    postEsrLatestEventView.setPositionNumber(POSITION_NUMBER);
    postEsrLatestEventView.setPositionId(POSITION_ID);
    postEsrLatestEventView.setFilename(FILE_NAME);
    postEsrLatestEventView.setStatus(STATUS);
    postEsrLatestEventView.setEventDateTime(eventTime);
  }

  @Test
  void shouldMapPostEsrLatestEventViewsToPostEsrEventDtos() {
    Set<PostEsrEventDto> postEsrEventDtos =
        mapper.postEsrLatestEventViewsToPostEsrEventDtos(Sets.newHashSet(postEsrLatestEventView));

    assertEquals(1, postEsrEventDtos.size());
    PostEsrEventDto postEsrEventDto = postEsrEventDtos.iterator().next();
    assertEquals(POST_ID, postEsrEventDto.getPostId());
    assertEquals(POSITION_NUMBER, postEsrEventDto.getPositionNumber());
    assertEquals(POSITION_ID, postEsrEventDto.getPositionId());
    assertEquals(FILE_NAME, postEsrEventDto.getFilename());
    assertEquals(STATUS, postEsrEventDto.getStatus());
    assertEquals(eventTime, postEsrEventDto.getEventDateTime());
  }
}
