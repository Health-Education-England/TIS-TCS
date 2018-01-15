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
    Set<String> gradeCodes = new HashSet<>();
    Set<String> siteCodes = new HashSet<>();
    Set<Long> traineeIds = new HashSet<>();
    placementViews.forEach(placementView -> {
      if (StringUtils.isNotBlank(placementView.getGradeAbbreviation())) {
        gradeCodes.add(placementView.getGradeAbbreviation());
      }
      if (StringUtils.isNotBlank(placementView.getSiteCode())) {
        siteCodes.add(placementView.getSiteCode());
      }
      if (placementView.getTraineeId() != null && placementView.getTraineeId() != 0) {
        traineeIds.add(placementView.getTraineeId());
      }
    });

    CompletableFuture<Void> futures = CompletableFuture.allOf(
            decorateGradesOnPlacement(gradeCodes, placementViews),
            decorateSitesOnPlacement(siteCodes, placementViews));

    decorateTraineeName(traineeIds, placementViews);

    futures.join();

    return placementViews;
  }

  protected CompletableFuture<Void> decorateGradesOnPlacement(Set<String> codes, List<PlacementViewDTO> placementViewDTOS) {
    return referenceService.doWithGradesAsync(codes, gradeMap -> {
      for (PlacementViewDTO pv : placementViewDTOS) {
        if (StringUtils.isNotBlank(pv.getGradeAbbreviation()) && gradeMap.containsKey(pv.getGradeAbbreviation())) {
          pv.setGradeName(gradeMap.get(pv.getGradeAbbreviation()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPlacement(Set<String> codes, List<PlacementViewDTO> placementViewDTOS) {
    return referenceService.doWithSitesAsync(codes, siteMap -> {
      for (PlacementViewDTO pv : placementViewDTOS) {
        if (StringUtils.isNotBlank(pv.getSiteCode()) && siteMap.containsKey(pv.getSiteCode())) {
          pv.setSiteName(siteMap.get(pv.getSiteCode()).getSiteName());
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
