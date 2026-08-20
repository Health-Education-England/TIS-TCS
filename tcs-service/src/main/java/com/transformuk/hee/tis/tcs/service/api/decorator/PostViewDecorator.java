package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used to decorate the Post View list with labels such as grade and site labels
 */
@Component
public class PostViewDecorator {
  private final AsyncReferenceService referenceService;

  @Autowired
  public PostViewDecorator(AsyncReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Decorates the given post views with sites and grades labels
   *
   * @param postViews the post views to decorate
   */
  public void decorate(List<PostViewDTO> postViews) {
    // collect all the codes from the list
    Set<Long> gradeIds = new HashSet<>();
    Set<Long> siteIds = new HashSet<>();
    Set<UUID> fundingSubtypeIds = new HashSet<>();
    postViews.forEach(postView -> {
      if (postView.getApprovedGradeId() != null) {
        gradeIds.add(postView.getApprovedGradeId());
      }
      if (postView.getPrimarySiteId() != null) {
        siteIds.add(postView.getPrimarySiteId());
      }
      if (CollectionUtils.isNotEmpty(postView.getFundingSubtypeIds())) {
        fundingSubtypeIds.addAll(postView.getFundingSubtypeIds());
      }
    });

    CompletableFuture.allOf(
        decorateGradesOnPost(gradeIds, postViews),
        decorateSitesOnPost(siteIds, postViews),
        decorateFundingSubtypesOnPost(fundingSubtypeIds, postViews))
        .join();
  }

  protected CompletableFuture<Void> decorateGradesOnPost(Set<Long> ids,
      List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithGradesAsync(ids, gradeMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (postView.getApprovedGradeId() != null && gradeMap
            .containsKey(postView.getApprovedGradeId())) {
          postView
              .setApprovedGradeCode(gradeMap.get(postView.getApprovedGradeId()).getAbbreviation());
          postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeId()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPost(Set<Long> ids,
      List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithSitesAsync(ids, siteMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (postView.getPrimarySiteId() != null && siteMap
            .containsKey(postView.getPrimarySiteId())) {
          postView.setPrimarySiteCode(siteMap.get(postView.getPrimarySiteId()).getSiteCode());
          postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteId()).getSiteName());
          postView.setPrimarySiteKnownAs(siteMap.get(postView.getPrimarySiteId()).getSiteKnownAs());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateFundingSubtypesOnPost(Set<UUID> ids,
      List<PostViewDTO> postViewDtos) {
    return referenceService.doWithFundingSubtypeAsync(ids, fundingSubtypeMap -> {
      for (PostViewDTO postView : postViewDtos) {
        if (postView.getFundingSubtypeIds() != null) {
          List<String> fundingSubtypeNames = postView.getFundingSubtypeIds().stream()
              .map(fundingSubtypeMap::get)
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
          postView.setFundingSubtypeNames(fundingSubtypeNames);
        }
      }
    });
  }
}
