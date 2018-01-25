package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Used to decorate the Post View list with labels such as grade and site labels
 */
@Component
public class PostViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PostViewDecorator.class);
  private AsyncReferenceService referenceService;

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
    postViews.forEach(postView -> {
      if (postView.getApprovedGradeId() != null) {
        gradeIds.add(postView.getApprovedGradeId());
      }
      if (postView.getPrimarySiteId() != null) {
        siteIds.add(postView.getPrimarySiteId());
      }
    });

    CompletableFuture.allOf(
            decorateGradesOnPost(gradeIds, postViews),
            decorateSitesOnPost(siteIds, postViews))
            .join();
  }

  protected CompletableFuture<Void> decorateGradesOnPost(Set<Long> ids, List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithGradesAsync(ids, gradeMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (postView.getApprovedGradeId() != null && gradeMap.containsKey(postView.getApprovedGradeId())) {
          postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeId()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPost(Set<Long> ids, List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithSitesAsync(ids, siteMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (postView.getPrimarySiteId() != null && siteMap.containsKey(postView.getPrimarySiteId())) {
          postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteId()).getSiteName());
        }
      }
    });
  }
}
