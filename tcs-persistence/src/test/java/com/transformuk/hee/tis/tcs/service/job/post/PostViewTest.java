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

package com.transformuk.hee.tis.tcs.service.job.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;

class PostViewTest {

  @Test
  void shouldHavePostsAsIndexName() {
    Document document = PostView.class.getAnnotation(Document.class);

    assertThat(document).isNotNull();
    assertThat(document.indexName()).isEqualTo("posts");
  }

  @Test
  void shouldUseIdFieldAsElasticsearchDocumentId() throws NoSuchFieldException {
    Field idField = PostView.class.getDeclaredField("id");

    assertThat(idField.getAnnotation(Id.class)).isNotNull();
    assertThat(idField.getType()).isEqualTo(Long.class);
  }

  @Test
  void shouldMapNationalPostNumberAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("nationalPostNumber");
  }

  @Test
  void shouldMapPrimarySpecialtyCodeAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("primarySpecialtyCode");
  }

  @Test
  void shouldMapStatusAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("status");
  }

  @Test
  void shouldMapFundingTypeAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("fundingType");
  }

  @Test
  void shouldMapOwnerAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("owner");
  }

  @Test
  void shouldMapIntrepidIdAsKeywordField() throws NoSuchFieldException {
    assertKeywordField("intrepidId");
  }

  @Test
  void shouldSupportGeneratedAccessorsMethod() {
    PostView postView = new PostView();

    postView.setId(243906L);
    postView.setNationalPostNumber("NWN/RM317/018/HT/002");
    postView.setStatus(Status.CURRENT);
    postView.setOwner("North West");

    assertThat(postView.getId()).isEqualTo(243906L);
    assertThat(postView.getNationalPostNumber()).isEqualTo("NWN/RM317/018/HT/002");
    assertThat(postView.getStatus()).isEqualTo(Status.CURRENT);
    assertThat(postView.getOwner()).isEqualTo("North West");
  }

  private void assertKeywordField(String fieldName) throws NoSuchFieldException {
    Field field = PostView.class.getDeclaredField(fieldName);

    org.springframework.data.elasticsearch.annotations.Field annotation =
        field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);

    assertThat(annotation).isNotNull();
    assertThat(annotation.type()).isEqualTo(FieldType.Keyword);
  }
}
