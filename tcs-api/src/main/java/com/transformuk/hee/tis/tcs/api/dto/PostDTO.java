package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Post entity.
 */
@Data
public class PostDTO implements Serializable {

  private static final long serialVersionUID = 3386087275695471446L;

  @NotNull(groups = Update.class, message = "Id must not be null when updating a post")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new post")
  private Long id;
  private String intrepidId;
  private String nationalPostNumber;
  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  private Status status;
  private Status fundingStatus;
  private PostSuffix suffix;
  @NotNull(message = "Owner is required", groups = {Update.class, Create.class})
  private String owner;
  private String postFamily;
  private PostDTO oldPost;
  private PostDTO newPost;
  private Set<PostSiteDTO> sites;
  @NotNull(message = "Employing body is required", groups = {Update.class, Create.class})
  private Long employingBodyId;
  @NotNull(message = "Training body is required", groups = {Update.class, Create.class})
  private Long trainingBodyId;
  private Set<PostGradeDTO> grades;
  private Set<PostSpecialtyDTO> specialties;
  private String trainingDescription;
  private String localPostNumber;
  private Set<PlacementDTO> placementHistory;
  @NotNull(message = "Programme is required", groups = {Update.class, Create.class})
  private Set<ProgrammeDTO> programmes;
  private Set<PostFundingDTO> fundings;
  private boolean bypassNPNGeneration;
  private Set<PostEsrEventDto> currentReconciledEvents;

  public PostDTO id(final Long id) {
    this.id = id;
    return this;
  }

  public PostDTO intrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public PostDTO nationalPostNumber(final String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
    return this;
  }

  public PostDTO status(final Status status) {
    this.status = status;
    return this;
  }

  public PostDTO suffix(final PostSuffix suffix) {
    this.suffix = suffix;
    return this;
  }

  public PostDTO owner(final String owner) {
    this.owner = owner;
    return this;
  }

  public PostDTO postFamily(final String postFamily) {
    this.postFamily = postFamily;
    return this;
  }

  public PostDTO oldPost(final PostDTO oldPost) {
    this.oldPost = oldPost;
    return this;
  }

  public PostDTO newPost(final PostDTO newPost) {
    this.newPost = newPost;
    return this;
  }

  public PostDTO employingBodyId(final Long employingBodyId) {
    this.employingBodyId = employingBodyId;
    return this;
  }

  public PostDTO trainingBodyId(final Long trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
    return this;
  }

  public PostDTO trainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
    return this;
  }

  public PostDTO localPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
    return this;
  }

  public PostDTO placementHistory(final Set<PlacementDTO> placementHistory) {
    this.placementHistory = placementHistory;
    return this;
  }

  public PostDTO programmes(final Set<ProgrammeDTO> programmes) {
    this.programmes = programmes;
    return this;
  }

  public PostDTO sites(final Set<PostSiteDTO> sites) {
    this.sites = sites;
    return this;
  }

  public PostDTO grades(final Set<PostGradeDTO> grades) {
    this.grades = grades;
    return this;
  }

  public PostDTO specialties(final Set<PostSpecialtyDTO> specialties) {
    this.specialties = specialties;
    return this;
  }

  public PostDTO currentReconciledEvents(final Set<PostEsrEventDto> currentReconciledEvents) {
    this.currentReconciledEvents = currentReconciledEvents;
    return this;
  }

  public void addFunding(final PostFundingDTO funding) {
    if (fundings == null) {
      fundings = new HashSet<>();
    }

    fundings.add(funding);
  }
}
