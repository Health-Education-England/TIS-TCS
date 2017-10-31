package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to decorate the Post View list with labels such as grade and site labels
 */
@Component
public class PostViewDecorator {

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
      if (postView.getApprovedGradeCode() != null && !postView.getApprovedGradeCode().isEmpty()) {
        gradeCodes.add(postView.getApprovedGradeCode());
      }
      if (postView.getPrimarySiteCode() != null && !postView.getPrimarySiteCode().isEmpty()) {
        siteCodes.add(postView.getPrimarySiteCode());
      }
    });

    // find the sites and grades we need
    Map<String, SiteDTO> siteMap = new HashMap<>();
    Map<String, GradeDTO> gradeMap = new HashMap<>();
    try {
      List<SiteDTO> sites = referenceService.findSitesIn(siteCodes);
      siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));
      List<GradeDTO> grades = referenceService.findGradesIn(gradeCodes);
      gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
    } catch (Exception e) {
      // if requests fail we just proceed with empty grade and site maps
      log.warn("Reference decorator call to sites or grades failed", e);
    }

    // decorate the views
    for (PostViewDTO postView : postViews) {
      if (postView.getApprovedGradeCode() != null && !postView.getApprovedGradeCode().isEmpty()) {
        if (gradeMap.containsKey(postView.getApprovedGradeCode())) {
          postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
        }
      }
      if (postView.getPrimarySiteCode() != null && !postView.getPrimarySiteCode().isEmpty()) {
        if (siteMap.containsKey(postView.getPrimarySiteCode())) {
          postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
        }
      }
    }
  }
}
