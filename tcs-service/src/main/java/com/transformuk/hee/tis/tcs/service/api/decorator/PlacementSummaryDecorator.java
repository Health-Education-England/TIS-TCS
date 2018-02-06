package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PlacementSummaryDecorator {

  private AsyncReferenceService referenceService;

  @Autowired
  public PlacementSummaryDecorator(AsyncReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Decorates the given placement views with sites and grades labels
   *
   * @param placementSummaryDTOS the placement views to decorate
   */
  public List<PlacementSummaryDTO> decorate(List<PlacementSummaryDTO> placementSummaryDTOS) {
    // collect all the codes from the list
    Set<Long> gradeIds = new HashSet<>();
    Set<Long> siteIds = new HashSet<>();
    placementSummaryDTOS.forEach(placementView -> {
      if (placementView.getGradeId() != null) {
        gradeIds.add(placementView.getGradeId().longValue());
      }
      if (placementView.getSiteId() != null) {
        siteIds.add(placementView.getSiteId().longValue());
      }
    });

    CompletableFuture<Void> futures = CompletableFuture.allOf(
            decorateGradesOnPlacement(gradeIds, placementSummaryDTOS),
            decorateSitesOnPlacement(siteIds, placementSummaryDTOS));

    futures.join();

    return placementSummaryDTOS;
  }

  protected CompletableFuture<Void> decorateGradesOnPlacement(Set<Long> ids, List<PlacementSummaryDTO> placementSummaryDTOS) {
    Set<Long> idsSet = ids.stream().map(id -> id.longValue()).collect(Collectors.toSet());
    return referenceService.doWithGradesAsync(idsSet, gradeMap -> {
      for (PlacementSummaryDTO pp : placementSummaryDTOS) {
        if (pp.getGradeId() != null && gradeMap.containsKey(pp.getGradeId().longValue())) {
          pp.setGradeName(gradeMap.get(pp.getGradeId().longValue()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPlacement(Set<Long> ids, List<PlacementSummaryDTO> placementSummaryDTOS) {
    Set<Long> idsSet = ids.stream().map(id -> id.longValue()).collect(Collectors.toSet());
    return referenceService.doWithSitesAsync(idsSet, siteMap -> {
      for (PlacementSummaryDTO pp : placementSummaryDTOS) {
        if (pp.getSiteId() != null && siteMap.containsKey(pp.getSiteId().longValue())) {
          pp.setSiteName(siteMap.get(pp.getSiteId().longValue()).getSiteName());
        }
      }
    });
  }
}
