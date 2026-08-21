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

package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mapstruct.factory.Mappers;

class PostViewMapperTest {

  private static final UUID UUID_1 = UUID.randomUUID();
  private static final UUID UUID_2 = UUID.randomUUID();
  private static final Long ID_1 = 1L;
  private static final Long ID_2 = 2L;
  private static final String SURNAME = "Smith";
  private static final String FORENAME = "John";
  private static final String NPN = "NPN-001";
  private static final String PROGRAMME_X = "Programme X";
  private static final String TARIFF = "Tariff";

  private PostViewMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(PostViewMapper.class);
  }

  // toDto

  @Test
  void toDtoShouldMapFundingSubtypeIdsToUuids() {
    PostView postView = new PostView();
    postView.setFundingSubtypeIds(List.of(UUID_1.toString(), UUID_2.toString()));

    PostViewDTO dto = mapper.toDto(postView);

    assertThat(dto.getFundingSubtypeIds()).containsExactly(UUID_1, UUID_2);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void toDtoShouldReturnEmptyListWhenFundingSubtypeIdsIsNullOrEmpty(List<String> fundingSubtypeIds) {
    PostView postView = new PostView();
    postView.setFundingSubtypeIds(fundingSubtypeIds);

    PostViewDTO dto = mapper.toDto(postView);

    assertThat(dto.getFundingSubtypeIds()).isEmpty();
  }

  @Test
  void toDtoShouldMapAllFields() {
    PostView postView = new PostView();
    postView.setId(ID_1);
    postView.setCurrentTraineeSurnames(SURNAME);
    postView.setCurrentTraineeForenames(FORENAME);
    postView.setNationalPostNumber(NPN);
    postView.setStatus(Status.CURRENT);
    postView.setProgrammeNames(List.of(PROGRAMME_X));
    postView.setFundingTypes(List.of(TARIFF));
    postView.setFundingSubtypeIds(List.of(UUID_1.toString()));

    PostViewDTO dto = mapper.toDto(postView);

    assertThat(dto.getId()).isEqualTo(ID_1);
    assertThat(dto.getCurrentTraineeSurname()).isEqualTo(SURNAME);
    assertThat(dto.getCurrentTraineeForenames()).isEqualTo(FORENAME);
    assertThat(dto.getNationalPostNumber()).isEqualTo(NPN);
    assertThat(dto.getStatus()).isEqualTo(Status.CURRENT);
    assertThat(dto.getProgrammeNames()).isEqualTo(PROGRAMME_X);
    assertThat(dto.getFundingType()).isEqualTo(TARIFF);
    assertThat(dto.getFundingSubtypeIds()).containsExactly(UUID_1);
  }

  // toDtos

  @Test
  void toDtosShouldMapEachPostView() {
    PostView postView1 = new PostView();
    postView1.setId(ID_1);
    postView1.setFundingSubtypeIds(List.of(UUID_1.toString()));

    PostView postView2 = new PostView();
    postView2.setId(ID_2);
    postView2.setFundingSubtypeIds(List.of(UUID_2.toString()));

    List<PostViewDTO> dtos = mapper.toDtos(List.of(postView1, postView2));

    assertThat(dtos).hasSize(2);
    assertThat(dtos.get(0).getId()).isEqualTo(ID_1);
    assertThat(dtos.get(0).getFundingSubtypeIds()).containsExactly(UUID_1);
    assertThat(dtos.get(1).getId()).isEqualTo(ID_2);
    assertThat(dtos.get(1).getFundingSubtypeIds()).containsExactly(UUID_2);
  }

  @Test
  void toDtosShouldReturnEmptyListWhenInputIsEmpty() {
    List<PostViewDTO> dtos = mapper.toDtos(Collections.emptyList());

    assertThat(dtos).isEmpty();
  }
}

