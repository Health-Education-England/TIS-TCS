package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import com.transformuk.hee.tis.tcs.service.repository.OwnerProjection;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Used to decorate the {@link com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO} with label data
 */
@Component
public class PlacementDetailsDecorator {

  private ReferenceService referenceService;
  private PersonBasicDetailsRepository personBasicDetailsRepository;
  private PostRepository postRepository;

  @Autowired
  public PlacementDetailsDecorator(ReferenceService referenceService, PersonBasicDetailsRepository personBasicDetailsRepository,
                                   PostRepository postRepository) {
    this.referenceService = referenceService;
    this.personBasicDetailsRepository = personBasicDetailsRepository;
    this.postRepository = postRepository;
  }

  /**
   * Decorates the given placement details with labels such as the trainee name, site name and grade name
   *
   * @param placementDetailsDTO the placement details to decorate
   */
  public PlacementDetailsDTO decorate(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO == null) return null;

    CompletableFuture<Void> traineeNameFuture = decorateTraineeName(placementDetailsDTO);
    CompletableFuture<Void> siteNameFuture = decorateSiteName(placementDetailsDTO);
    CompletableFuture<Void> gradeNameFuture = decorateGradeName(placementDetailsDTO);
    CompletableFuture<Void> ownerFuture = decorateOwner(placementDetailsDTO);

    CompletableFuture.allOf(traineeNameFuture, siteNameFuture, gradeNameFuture, ownerFuture).join();
    return placementDetailsDTO;
  }

  @Async
  protected CompletableFuture<Void> decorateTraineeName(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO.getTraineeId() != null && placementDetailsDTO.getTraineeId() != 0) {
      PersonBasicDetails bdt = personBasicDetailsRepository.findOne(placementDetailsDTO.getTraineeId());
      if (bdt != null) {
        placementDetailsDTO.setTraineeFirstName(bdt.getFirstName());
        placementDetailsDTO.setTraineeLastName(bdt.getLastName());
        if (bdt.getGmcDetails() != null) {
          placementDetailsDTO.setTraineeGmcNumber(bdt.getGmcDetails().getGmcNumber());
        }
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateSiteName(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO.getSiteCode() != null && !placementDetailsDTO.getSiteCode().isEmpty()) {
      List<SiteDTO> sites = referenceService.findSitesIn(Sets.newHashSet(placementDetailsDTO.getSiteCode()));
      if (sites != null && !sites.isEmpty()) {
        placementDetailsDTO.setSiteName(sites.get(0).getSiteName());
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateGradeName(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO.getGradeAbbreviation() != null && !placementDetailsDTO.getGradeAbbreviation().isEmpty()) {
      List<GradeDTO> grades = referenceService.findGradesIn(Sets.newHashSet(placementDetailsDTO.getGradeAbbreviation()));
      if (grades != null && !grades.isEmpty()) {
        placementDetailsDTO.setGradeName(grades.get(0).getName());
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateOwner(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO.getPostId() != null && placementDetailsDTO.getPostId() != 0) {
      OwnerProjection op = postRepository.findPostById(placementDetailsDTO.getPostId());
      if (op != null) {
        placementDetailsDTO.setOwner(op.getManagingLocalOffice());
        placementDetailsDTO.setNationalPostNumber(op.getNationalPostNumber());
      }
    }
    return CompletableFuture.completedFuture(null);
  }

}
