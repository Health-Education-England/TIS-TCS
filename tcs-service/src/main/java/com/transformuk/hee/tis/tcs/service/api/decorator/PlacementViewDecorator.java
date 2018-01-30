package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Used to decorate the Placement View list with labels such as grade and site labels
 */
@Component
public class PlacementViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PlacementViewDecorator.class);
  private AsyncReferenceService referenceService;
  private PersonBasicDetailsRepositoryAccessor personBasicDetailsRepository;

  @Autowired
  public PlacementViewDecorator(AsyncReferenceService referenceService,
                                PersonBasicDetailsRepositoryAccessor personBasicDetailsRepository) {
    this.referenceService = referenceService;
    this.personBasicDetailsRepository = personBasicDetailsRepository;
  }

  /**
   * Decorates the given placement views with sites and grades labels
   *
   * @param placementViews the placement views to decorate
   */
  public List<PlacementViewDTO> decorate(List<PlacementViewDTO> placementViews) {
    // collect all the codes from the list
    Set<Long> gradeIds = new HashSet<>();
    Set<Long> siteIds = new HashSet<>();
    Set<Long> traineeIds = new HashSet<>();
    placementViews.forEach(placementView -> {
      if (placementView.getGradeId() != null) {
        gradeIds.add(placementView.getGradeId());
      }
      if (placementView.getSiteId() != null) {
        siteIds.add(placementView.getSiteId());
      }
      if (placementView.getTraineeId() != null && placementView.getTraineeId() != 0) {
        traineeIds.add(placementView.getTraineeId());
      }
    });

    CompletableFuture<Void> futures = CompletableFuture.allOf(
            decorateGradesOnPlacement(gradeIds, placementViews),
            decorateSitesOnPlacement(siteIds, placementViews));

    decorateTraineeName(traineeIds, placementViews);

    futures.join();

    return placementViews;
  }

  protected CompletableFuture<Void> decorateGradesOnPlacement(Set<Long> ids, List<PlacementViewDTO> placementViewDTOS) {
    return referenceService.doWithGradesAsync(ids, gradeMap -> {
      for (PlacementViewDTO pv : placementViewDTOS) {
        if (pv.getGradeId() != null && gradeMap.containsKey(pv.getGradeId())) {
          pv.setGradeAbbreviation(gradeMap.get(pv.getGradeId()).getAbbreviation());
          pv.setGradeName(gradeMap.get(pv.getGradeId()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPlacement(Set<Long> ids, List<PlacementViewDTO> placementViewDTOS) {
    return referenceService.doWithSitesAsync(ids, siteMap -> {
      for (PlacementViewDTO pv : placementViewDTOS) {
        if (pv.getSiteId() != null && siteMap.containsKey(pv.getSiteId())) {
          pv.setSiteCode(siteMap.get(pv.getSiteId()).getSiteCode());
          pv.setSiteName(siteMap.get(pv.getSiteId()).getSiteName());
        }
      }
    });
  }

  protected void decorateTraineeName(Set<Long> traineeIds, List<PlacementViewDTO> placementViewDTOS) {
    personBasicDetailsRepository.doWithPersonBasicDetailsSet(traineeIds, detailsMap -> {
      for (PlacementViewDTO pv : placementViewDTOS) {
        Long traineeId = pv.getTraineeId();
        if (traineeId != null && traineeId != 0 && detailsMap.containsKey(traineeId)) {
          PersonBasicDetails bd = detailsMap.get(pv.getTraineeId());
          pv.setTraineeFirstName(bd.getFirstName());
          pv.setTraineeLastName(bd.getLastName());
          pv.setTraineeGmcNumber(bd.getGmcDetails() != null ? bd.getGmcDetails().getGmcNumber() : null);
        }
      }
    });
  }
}
