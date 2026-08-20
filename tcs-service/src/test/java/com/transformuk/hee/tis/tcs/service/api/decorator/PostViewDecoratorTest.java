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

package com.transformuk.hee.tis.tcs.service.api.decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostViewDecoratorTest {

  private static final long GRADE_ID = 1L;
  private static final long SITE_ID = 2L;
  private static final String GRADE_CODE = "Grade code";
  private static final String GRADE_NAME = "Grade name";
  private static final String SITE_CODE = "Site code";
  private static final String SITE_NAME = "Site name";
  private static final String SITE_KNOWN_AS = "Site Known As";
  private static final UUID FUNDING_SUBTYPE_ID_1 = UUID.randomUUID();
  private static final UUID FUNDING_SUBTYPE_ID_2 = UUID.randomUUID();
  private static final UUID UNKNOWN_FUNDING_SUBTYPE_ID = UUID.randomUUID();
  private static final String FUNDING_SUBTYPE_NAME_1 = "Funding subtype 1";
  private static final String FUNDING_SUBTYPE_NAME_2 = "Funding subtype 2";

  @Mock
  private AsyncReferenceService referenceService;

  @InjectMocks
  private PostViewDecorator postViewDecorator;

  @Test
  void shouldPopulateFundingSubtypeNamesInOrderAndIgnoreUnknownIds() {
    Set<UUID> ids = Set.of(FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_ID_2,
        UNKNOWN_FUNDING_SUBTYPE_ID);
    PostViewDTO postViewWithFundingSubtypes = new PostViewDTO();
    postViewWithFundingSubtypes.setFundingSubtypeIds(
        List.of(FUNDING_SUBTYPE_ID_1, UNKNOWN_FUNDING_SUBTYPE_ID, FUNDING_SUBTYPE_ID_2));

    PostViewDTO postViewWithoutFundingSubtypes = new PostViewDTO();
    List<PostViewDTO> postViews = List.of(postViewWithFundingSubtypes,
        postViewWithoutFundingSubtypes);

    when(referenceService.doWithFundingSubtypeAsync(eq(ids), any()))
        .thenAnswer(invocation -> {
          Consumer<Map<UUID, String>> consumer = invocation.getArgument(1);
          consumer.accept(Map.of(
              FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_NAME_1,
              FUNDING_SUBTYPE_ID_2, FUNDING_SUBTYPE_NAME_2
          ));
          return CompletableFuture.completedFuture(null);
        });

    CompletableFuture<Void> result = postViewDecorator.decorateFundingSubtypesOnPost(ids,
        postViews);

    assertTrue(result.isDone());
    verify(referenceService).doWithFundingSubtypeAsync(eq(ids), any());
    assertEquals(List.of(FUNDING_SUBTYPE_NAME_1, FUNDING_SUBTYPE_NAME_2),
        postViewWithFundingSubtypes.getFundingSubtypeNames());
    assertNull(postViewWithoutFundingSubtypes.getFundingSubtypeNames());
  }

  @Test
  void shouldDecoratePostsUsingCollectedGradeSiteAndFundingSubtypeIds() {
    Set<Long> gradeIds = Set.of(GRADE_ID);
    Set<Long> siteIds = Set.of(SITE_ID);
    Set<UUID> fundingSubtypeIds = Set.of(FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_ID_2);

    PostViewDTO postViewDto1 = new PostViewDTO();
    postViewDto1.setApprovedGradeId(GRADE_ID);
    postViewDto1.setPrimarySiteId(SITE_ID);
    postViewDto1.setFundingSubtypeIds(List.of(FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_ID_2));

    PostViewDTO postViewDto2 = new PostViewDTO();
    postViewDto2.setApprovedGradeId(GRADE_ID);
    postViewDto2.setPrimarySiteId(SITE_ID);
    postViewDto2.setFundingSubtypeIds(List.of(FUNDING_SUBTYPE_ID_2));

    PostViewDTO postViewDto3 = new PostViewDTO();

    List<PostViewDTO> postViews = List.of(postViewDto1, postViewDto2, postViewDto3);

    when(referenceService.doWithGradesAsync(eq(gradeIds), any()))
        .thenAnswer(invocation -> {
          Consumer<Map<Long, GradeDTO>> consumer = invocation.getArgument(1);
          GradeDTO grade = new GradeDTO();
          grade.setId(GRADE_ID);
          grade.setAbbreviation(GRADE_CODE);
          grade.setName(GRADE_NAME);
          consumer.accept(Map.of(GRADE_ID, grade));
          return CompletableFuture.completedFuture(null);
        });

    when(referenceService.doWithSitesAsync(eq(siteIds), any()))
        .thenAnswer(invocation -> {
          Consumer<Map<Long, SiteDTO>> consumer = invocation.getArgument(1);
          SiteDTO site = new SiteDTO();
          site.setId(SITE_ID);
          site.setSiteCode(SITE_CODE);
          site.setSiteName(SITE_NAME);
          site.setSiteKnownAs(SITE_KNOWN_AS);
          consumer.accept(Map.of(SITE_ID, site));
          return CompletableFuture.completedFuture(null);
        });

    when(referenceService.doWithFundingSubtypeAsync(eq(fundingSubtypeIds), any()))
        .thenAnswer(invocation -> {
          Consumer<Map<UUID, String>> consumer = invocation.getArgument(1);
          consumer.accept(Map.of(
              FUNDING_SUBTYPE_ID_1, FUNDING_SUBTYPE_NAME_1,
              FUNDING_SUBTYPE_ID_2, FUNDING_SUBTYPE_NAME_2
          ));
          return CompletableFuture.completedFuture(null);
        });

    postViewDecorator.decorate(postViews);

    verify(referenceService).doWithGradesAsync(eq(gradeIds), any());
    verify(referenceService).doWithSitesAsync(eq(siteIds), any());
    verify(referenceService).doWithFundingSubtypeAsync(eq(fundingSubtypeIds), any());

    assertEquals(GRADE_CODE, postViewDto1.getApprovedGradeCode());
    assertEquals(GRADE_NAME, postViewDto1.getApprovedGradeName());
    assertEquals(SITE_CODE, postViewDto1.getPrimarySiteCode());
    assertEquals(SITE_NAME, postViewDto1.getPrimarySiteName());
    assertEquals(SITE_KNOWN_AS, postViewDto1.getPrimarySiteKnownAs());
    assertEquals(List.of(FUNDING_SUBTYPE_NAME_1, FUNDING_SUBTYPE_NAME_2),
        postViewDto1.getFundingSubtypeNames());

    assertEquals(GRADE_CODE, postViewDto2.getApprovedGradeCode());
    assertEquals(GRADE_NAME, postViewDto2.getApprovedGradeName());
    assertEquals(SITE_CODE, postViewDto2.getPrimarySiteCode());
    assertEquals(SITE_NAME, postViewDto2.getPrimarySiteName());
    assertEquals(SITE_KNOWN_AS, postViewDto2.getPrimarySiteKnownAs());
    assertEquals(List.of(FUNDING_SUBTYPE_NAME_2), postViewDto2.getFundingSubtypeNames());

    assertNull(postViewDto3.getApprovedGradeCode());
    assertNull(postViewDto3.getApprovedGradeName());
    assertNull(postViewDto3.getPrimarySiteCode());
    assertNull(postViewDto3.getPrimarySiteName());
    assertNull(postViewDto3.getPrimarySiteKnownAs());
    assertNull(postViewDto3.getFundingSubtypeNames());
  }
}


