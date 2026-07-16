package com.transformuk.hee.tis.tcs.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PostDeletedEventTest {

  private static final Long POST_ID = 100L;
  private static final Long DIFFERENT_POST_ID = 200L;

  @Test
  void shouldCreateEventWithPostId() {
    PostDeletedEvent event = new PostDeletedEvent(POST_ID);

    assertEquals(POST_ID, event.getPostId());
    assertEquals(POST_ID, event.getSource());
  }

  @Test
  void shouldEvaluateEqualityAndHashCode() {
    PostDeletedEvent event = new PostDeletedEvent(POST_ID);
    PostDeletedEvent equalEvent = new PostDeletedEvent(POST_ID);
    PostDeletedEvent differentEvent = new PostDeletedEvent(DIFFERENT_POST_ID);

    assertEquals(event, equalEvent);
    assertEquals(event.hashCode(), equalEvent.hashCode());
    assertNotEquals(event, differentEvent);
    assertNotEquals(null, event);
  }
}


