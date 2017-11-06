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

  public static final int LATCH_COUNT = 2;
  private static final Logger log = LoggerFactory.getLogger(PostViewDecorator.class);
  private ReferenceService referenceService;

  @Autowired
  public PostViewDecorator(ReferenceService referenceService) {
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

    CompletableFuture<Void> gradesFuture = decorateGradesOnPost(gradeCodes, postViews);
    CompletableFuture<Void> sitesFuture = decorateSitesOnPost(siteCodes, postViews);

    CompletableFuture.allOf(gradesFuture, sitesFuture).join();

  }

  @Async
  protected CompletableFuture<Void> decorateGradesOnPost(Set<String> codes, List<PostViewDTO> postViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<GradeDTO> grades = referenceService.findGradesIn(codes);
        Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
        for (PostViewDTO postView : postViewDTOS) {
          if (StringUtils.isNotBlank(postView.getApprovedGradeCode()) && gradeMap.containsKey(postView.getApprovedGradeCode())) {
            postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to grades failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateSitesOnPost(Set<String> codes, List<PostViewDTO> postViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<SiteDTO> sites = referenceService.findSitesIn(codes);
        Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

        for (PostViewDTO postView : postViewDTOS) {
          if (StringUtils.isNotBlank(postView.getPrimarySiteCode()) && siteMap.containsKey(postView.getPrimarySiteCode())) {
            postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to sites failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

}
