package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
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
 * Used to decorate the Placement View list with labels such as grade and site labels
 */
@Component
public class PlacementViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PlacementViewDecorator.class);
  private ReferenceService referenceService;

  @Autowired
  public PlacementViewDecorator(ReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Decorates the given placement views with sites and grades labels
   *
   * @param placementViews the placement views to decorate
   */
  public List<PlacementViewDTO> decorate(List<PlacementViewDTO> placementViews) {
    // collect all the codes from the list
    Set<String> gradeCodes = new HashSet<>();
    Set<String> siteCodes = new HashSet<>();
    placementViews.forEach(placementView -> {
      if (StringUtils.isNotBlank(placementView.getGradeAbbreviation())) {
        gradeCodes.add(placementView.getGradeAbbreviation());
      }
      if (StringUtils.isNotBlank(placementView.getSiteCode())) {
        siteCodes.add(placementView.getSiteCode());
      }
    });

    CompletableFuture<Void> gradesFuture = decorateGradesOnPlacement(gradeCodes, placementViews);
    CompletableFuture<Void> sitesFuture = decorateSitesOnPlacement(siteCodes, placementViews);

    CompletableFuture.allOf(gradesFuture, sitesFuture).join();
    return placementViews;
  }

  @Async
  protected CompletableFuture<Void> decorateGradesOnPlacement(Set<String> codes, List<PlacementViewDTO> placementViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<GradeDTO> grades = referenceService.findGradesIn(codes);
        Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
        for (PlacementViewDTO placementView : placementViewDTOS) {
          if (StringUtils.isNotBlank(placementView.getGradeAbbreviation()) && gradeMap.containsKey(placementView.getGradeAbbreviation())) {
            placementView.setGradeName(gradeMap.get(placementView.getGradeAbbreviation()).getName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to grades failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateSitesOnPlacement(Set<String> codes, List<PlacementViewDTO> placementViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<SiteDTO> sites = referenceService.findSitesIn(codes);
        Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

        for (PlacementViewDTO placementView : placementViewDTOS) {
          if (StringUtils.isNotBlank(placementView.getSiteCode()) && siteMap.containsKey(placementView.getSiteCode())) {
            placementView.setSiteName(siteMap.get(placementView.getSiteCode()).getSiteName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to sites failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

}
