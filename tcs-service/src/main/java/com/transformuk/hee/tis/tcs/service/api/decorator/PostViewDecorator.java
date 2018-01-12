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
    Set<String> gradeCodes = new HashSet<>();
    Set<String> siteCodes = new HashSet<>();
    postViews.forEach(postView -> {
      if (StringUtils.isNotBlank(postView.getApprovedGradeCode())) {
        gradeCodes.add(postView.getApprovedGradeCode());
      }
      if (StringUtils.isNotBlank(postView.getPrimarySiteCode())) {
        siteCodes.add(postView.getPrimarySiteCode());
      }
    });

    CompletableFuture.allOf(
            decorateGradesOnPost(gradeCodes, postViews),
            decorateSitesOnPost(siteCodes, postViews))
            .join();
  }

  protected CompletableFuture<Void> decorateGradesOnPost(Set<String> codes, List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithGradesAsync(codes, gradeMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (StringUtils.isNotBlank(postView.getApprovedGradeCode()) && gradeMap.containsKey(postView.getApprovedGradeCode())) {
          postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPost(Set<String> codes, List<PostViewDTO> postViewDTOS) {
    return referenceService.doWithSitesAsync(codes, siteMap -> {
      for (PostViewDTO postView : postViewDTOS) {
        if (StringUtils.isNotBlank(postView.getPrimarySiteCode()) && siteMap.containsKey(postView.getPrimarySiteCode())) {
          postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
        }
      }
    });
  }
}
