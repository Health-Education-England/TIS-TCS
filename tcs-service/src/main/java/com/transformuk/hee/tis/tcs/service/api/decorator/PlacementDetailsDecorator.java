package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.service.repository.OwnerProjection;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Used to decorate the {@link com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO} with label data
 */
@Component
public class PlacementDetailsDecorator {

  private PostRepository postRepository;
  private AsyncReferenceService referenceService;
  private PersonBasicDetailsRepositoryAccessor personDetailsRepo;

  @Autowired
  public PlacementDetailsDecorator(AsyncReferenceService asyncReferenceService, PersonBasicDetailsRepositoryAccessor personBasicDetailsRepositoryAccessor,
                                   PostRepository postRepository) {
    this.personDetailsRepo = personBasicDetailsRepositoryAccessor;
    this.postRepository = postRepository;
    this.referenceService = asyncReferenceService;
  }

  /**
   * Decorates the given placement details with labels such as the trainee name, site name and grade name
   *
   * @param placementDetailsDTO the placement details to decorate
   */
  public PlacementDetailsDTO decorate(PlacementDetailsDTO placementDetailsDTO) {
    if (placementDetailsDTO == null) return null;

    CompletableFuture<Void> futures = CompletableFuture.allOf(
            decorateSiteName(placementDetailsDTO),
            decorateGradeName(placementDetailsDTO));

    decorateTraineeName(placementDetailsDTO);
    decorateOwner(placementDetailsDTO);

    futures.join();
    return placementDetailsDTO;
  }

  protected void decorateTraineeName(PlacementDetailsDTO placementDetailsDTO) {
    personDetailsRepo.doWithPersonBasicDetails(
        placementDetailsDTO.getTraineeId(),
        personDetails -> {
          placementDetailsDTO.setTraineeFirstName(personDetails.getFirstName());
          placementDetailsDTO.setTraineeLastName(personDetails.getLastName());
          if (personDetails.getGmcDetails() != null) {
            placementDetailsDTO.setTraineeGmcNumber(personDetails.getGmcDetails().getGmcNumber());
          }
        });
  }
  
  protected void decorateOwner(PlacementDetailsDTO placementDetailsDTO) {
    Long postId = placementDetailsDTO.getPostId();
    if (postId != null && postId != 0) {
      OwnerProjection ownerProjection = postRepository.findPostById(postId);
      if (ownerProjection != null) {
        placementDetailsDTO.setOwner(ownerProjection.getOwner());
        placementDetailsDTO.setNationalPostNumber(ownerProjection.getNationalPostNumber());
      }
    }
  }

  protected CompletableFuture<Void> decorateSiteName(PlacementDetailsDTO placementDetailsDTO) {
    return referenceService.doWithSitesAsync(
            () -> !StringUtils.isEmpty(placementDetailsDTO.getSiteCode()),
            Collections.singleton(placementDetailsDTO.getSiteId()),
            sites -> {
              placementDetailsDTO.setSiteName(sites.values().iterator().next().getSiteName());
            });
  }

  protected CompletableFuture<Void> decorateGradeName(PlacementDetailsDTO placementDetailsDTO) {
    return referenceService.doWithGradesAsync(
            () -> !StringUtils.isEmpty(placementDetailsDTO.getGradeAbbreviation()),
            Collections.singleton(placementDetailsDTO.getGradeId()), grades -> {
              placementDetailsDTO.setGradeName(grades.values().iterator().next().getName());
            });
  }
}
