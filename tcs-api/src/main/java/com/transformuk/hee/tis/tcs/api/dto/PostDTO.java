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

/**
 * A DTO for the Post entity.
 */
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
  private PostSuffix suffix;
  @NotNull(message = "Owner is required", groups = {Update.class, Create.class})
  private String owner;
  private String postFamily;
  private PostDTO oldPost;
  private PostDTO newPost;
  private Set<PostSiteDTO> sites;
  private Long employingBodyId;
  private Long trainingBodyId;
  private Set<PostGradeDTO> grades;
  private Set<PostSpecialtyDTO> specialties;
  private String trainingDescription;
  private String localPostNumber;
  private Set<PlacementDTO> placementHistory;
  private Set<ProgrammeDTO> programmes;
  private Set<PostFundingDTO> fundings;
  private boolean bypassNPNGeneration;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public PostDTO id(final Long id) {
    this.id = id;
    return this;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public PostDTO intrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(final String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public PostDTO nationalPostNumber(final String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  public PostDTO status(final Status status) {
    this.status = status;
    return this;
  }

  public PostSuffix getSuffix() {
    return suffix;
  }

  public void setSuffix(final PostSuffix suffix) {
    this.suffix = suffix;
  }

  public PostDTO suffix(final PostSuffix suffix) {
    this.suffix = suffix;
    return this;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(final String owner) {
    this.owner = owner;
  }

  public PostDTO owner(final String owner) {
    this.owner = owner;
    return this;
  }

  public String getPostFamily() {
    return postFamily;
  }

  public void setPostFamily(final String postFamily) {
    this.postFamily = postFamily;
  }

  public PostDTO postFamily(final String postFamily) {
    this.postFamily = postFamily;
    return this;
  }

  public PostDTO getOldPost() {
    return oldPost;
  }

  public void setOldPost(final PostDTO oldPost) {
    this.oldPost = oldPost;
  }

  public PostDTO oldPost(final PostDTO oldPost) {
    this.oldPost = oldPost;
    return this;
  }

  public PostDTO getNewPost() {
    return newPost;
  }

  public void setNewPost(final PostDTO newPost) {
    this.newPost = newPost;
  }

  public PostDTO newPost(final PostDTO newPost) {
    this.newPost = newPost;
    return this;
  }

  public Long getEmployingBodyId() {
    return employingBodyId;
  }

  public void setEmployingBodyId(final Long employingBodyId) {
    this.employingBodyId = employingBodyId;
  }

  public PostDTO employingBodyId(final Long employingBodyId) {
    this.employingBodyId = employingBodyId;
    return this;
  }

  public Long getTrainingBodyId() {
    return trainingBodyId;
  }

  public void setTrainingBodyId(final Long trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
  }

  public PostDTO trainingBodyId(final Long trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
    return this;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public PostDTO trainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
    return this;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public PostDTO localPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
    return this;
  }

  public Set<PlacementDTO> getPlacementHistory() {
    return placementHistory;
  }

  public void setPlacementHistory(final Set<PlacementDTO> placementHistory) {
    this.placementHistory = placementHistory;
  }

  public PostDTO placementHistory(final Set<PlacementDTO> placementHistory) {
    this.placementHistory = placementHistory;
    return this;
  }

  public Set<ProgrammeDTO> getProgrammes() {
    return programmes;
  }

  public void setProgrammes(final Set<ProgrammeDTO> programmes) {
    this.programmes = programmes;
  }

  public PostDTO programmes(final Set<ProgrammeDTO> programmes) {
    this.programmes = programmes;
    return this;
  }

  public Set<PostSiteDTO> getSites() {
    return sites;
  }

  public void setSites(final Set<PostSiteDTO> sites) {
    this.sites = sites;
  }

  public PostDTO sites(final Set<PostSiteDTO> sites) {
    this.sites = sites;
    return this;
  }

  public Set<PostGradeDTO> getGrades() {
    return grades;
  }

  public void setGrades(final Set<PostGradeDTO> grades) {
    this.grades = grades;
  }

  public PostDTO grades(final Set<PostGradeDTO> grades) {
    this.grades = grades;
    return this;
  }

  public Set<PostSpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(final Set<PostSpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  public PostDTO specialties(final Set<PostSpecialtyDTO> specialties) {
    this.specialties = specialties;
    return this;
  }

  public Set<PostFundingDTO> getFundings() {
    return fundings;
  }

  public void setFundings(final Set<PostFundingDTO> fundings) {
    this.fundings = fundings;
  }

  public void addFunding(final PostFundingDTO funding) {
    if (fundings == null) {
      fundings = new HashSet<>();
    }

    fundings.add(funding);
  }

  public boolean isBypassNPNGeneration() {
    return bypassNPNGeneration;
  }

  public void setBypassNPNGeneration(final boolean bypassNPNGeneration) {
    this.bypassNPNGeneration = bypassNPNGeneration;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final PostDTO postDTO = (PostDTO) o;

    if (bypassNPNGeneration != postDTO.bypassNPNGeneration) {
      return false;
    }
    if (id != null ? !id.equals(postDTO.id) : postDTO.id != null) {
      return false;
    }
    if (intrepidId != null ? !intrepidId.equals(postDTO.intrepidId) : postDTO.intrepidId != null) {
      return false;
    }
    if (nationalPostNumber != null ? !nationalPostNumber.equals(postDTO.nationalPostNumber)
        : postDTO.nationalPostNumber != null) {
      return false;
    }
    if (status != postDTO.status) {
      return false;
    }
    if (suffix != postDTO.suffix) {
      return false;
    }
    if (owner != null ? !owner.equals(postDTO.owner) : postDTO.owner != null) {
      return false;
    }
    if (postFamily != null ? !postFamily.equals(postDTO.postFamily) : postDTO.postFamily != null) {
      return false;
    }
    if (oldPost != null ? !oldPost.equals(postDTO.oldPost) : postDTO.oldPost != null) {
      return false;
    }
    if (newPost != null ? !newPost.equals(postDTO.newPost) : postDTO.newPost != null) {
      return false;
    }
    if (sites != null ? !sites.equals(postDTO.sites) : postDTO.sites != null) {
      return false;
    }
    if (employingBodyId != null ? !employingBodyId.equals(postDTO.employingBodyId)
        : postDTO.employingBodyId != null) {
      return false;
    }
    if (trainingBodyId != null ? !trainingBodyId.equals(postDTO.trainingBodyId)
        : postDTO.trainingBodyId != null) {
      return false;
    }
    if (grades != null ? !grades.equals(postDTO.grades) : postDTO.grades != null) {
      return false;
    }
    if (specialties != null ? !specialties.equals(postDTO.specialties)
        : postDTO.specialties != null) {
      return false;
    }
    if (trainingDescription != null ? !trainingDescription.equals(postDTO.trainingDescription)
        : postDTO.trainingDescription != null) {
      return false;
    }
    if (localPostNumber != null ? !localPostNumber.equals(postDTO.localPostNumber)
        : postDTO.localPostNumber != null) {
      return false;
    }
    if (placementHistory != null ? !placementHistory.equals(postDTO.placementHistory)
        : postDTO.placementHistory != null) {
      return false;
    }
    if (programmes != null ? !programmes.equals(postDTO.programmes) : postDTO.programmes != null) {
      return false;
    }
    return fundings != null ? fundings.equals(postDTO.fundings) : postDTO.fundings == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + (postFamily != null ? postFamily.hashCode() : 0);
    result = 31 * result + (oldPost != null ? oldPost.hashCode() : 0);
    result = 31 * result + (newPost != null ? newPost.hashCode() : 0);
    result = 31 * result + (sites != null ? sites.hashCode() : 0);
    result = 31 * result + (employingBodyId != null ? employingBodyId.hashCode() : 0);
    result = 31 * result + (trainingBodyId != null ? trainingBodyId.hashCode() : 0);
    result = 31 * result + (grades != null ? grades.hashCode() : 0);
    result = 31 * result + (specialties != null ? specialties.hashCode() : 0);
    result = 31 * result + (trainingDescription != null ? trainingDescription.hashCode() : 0);
    result = 31 * result + (localPostNumber != null ? localPostNumber.hashCode() : 0);
    result = 31 * result + (placementHistory != null ? placementHistory.hashCode() : 0);
    result = 31 * result + (programmes != null ? programmes.hashCode() : 0);
    result = 31 * result + (fundings != null ? fundings.hashCode() : 0);
    result = 31 * result + (bypassNPNGeneration ? 1 : 0);
    return result;
  }
}
