/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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


