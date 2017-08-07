package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A Post.
 */
@Entity
public class Post implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "nationalPostNumber")
  private String nationalPostNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "suffix")
  private PostSuffix suffix;

  @Column(name = "managingLocalOffice")
  private String managingLocalOffice;

  @Column(name = "postFamily")
  private String postFamily;

  @OneToOne
  @JoinColumn(name = "oldPostId")
  private Post oldPost;

  @OneToOne
  @JoinColumn(name = "newPostId")
  private Post newPost;

  // Entity Site defined in the Reference service
  @Column(name = "mainSiteLocatedId")
  private String mainSiteLocatedId;

  // Entity Site defined in the Reference service
  @ElementCollection
  @CollectionTable(
      name = "PostOtherSites",
      joinColumns = @JoinColumn(name = "postId", referencedColumnName = "id"))
  @Column(name = "siteId")
  private Set<String> otherSiteIds;

  // Entity Trust defined in the Reference service
  @Column(name = "employingBodyId")
  private String employingBodyId;

  // Entity Trust defined in the Reference service
  @Column(name = "trainingBodyId")
  private String trainingBodyId;

  // Entity Grade defined in the Reference service
  @Column(name = "approvedGradeId")
  private String approvedGradeId;

  @ElementCollection
  @CollectionTable(
      name = "PostOtherGrades",
      joinColumns = @JoinColumn(name = "postId", referencedColumnName = "id"))
  @Column(name = "gradeId")
  private Set<String> otherGradeIds;

  @OneToOne
  @JoinColumn(name = "specialtyId")
  private Specialty specialty;

  @OneToMany
  @JoinTable(name = "PostOtherSpecialties",
      joinColumns = @JoinColumn(name = "postId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "specialtyId", referencedColumnName = "id"))
  private Set<Specialty> otherSpecialties;

  @OneToOne
  @JoinColumn(name = "subspecialtyId")
  private Specialty subspecialty;

  @Column(name = "trainingDescription")
  private String trainingDescription;

  @Column(name = "localPostNumber")
  private String localPostNumber;

  @OneToMany
  @JoinTable(name = "PostPlacementHistory",
      joinColumns = @JoinColumn(name = "postId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "placementId", referencedColumnName = "id"))
  private Set<Placement> placementHistory;

  @OneToOne
  @JoinColumn(name = "programmeId")
  private Programme programmes;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public Post nationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Post status(Status status) {
    this.status = status;
    return this;
  }

  public PostSuffix getSuffix() {
    return suffix;
  }

  public void setSuffix(PostSuffix suffix) {
    this.suffix = suffix;
  }

  public Post suffix(PostSuffix suffix) {
    this.suffix = suffix;
    return this;
  }

  public String getManagingLocalOffice() {
    return managingLocalOffice;
  }

  public void setManagingLocalOffice(String managingLocalOffice) {
    this.managingLocalOffice = managingLocalOffice;
  }

  public Post managingLocalOffice(String managingLocalOffice) {
    this.managingLocalOffice = managingLocalOffice;
    return this;
  }

  public String getPostFamily() {
    return postFamily;
  }

  public void setPostFamily(String postFamily) {
    this.postFamily = postFamily;
  }

  public Post postFamily(String postFamily) {
    this.postFamily = postFamily;
    return this;
  }

  public Post getOldPost() {
    return oldPost;
  }

  public void setOldPost(Post oldPost) {
    this.oldPost = oldPost;
  }

  public Post oldPost(Post oldPost) {
    this.oldPost = oldPost;
    return this;
  }

  public Post getNewPost() {
    return newPost;
  }

  public void setNewPost(Post newPost) {
    this.newPost = newPost;
  }

  public Post newPost(Post newPost) {
    this.newPost = newPost;
    return this;
  }

  public String getMainSiteLocatedId() {
    return mainSiteLocatedId;
  }

  public void setMainSiteLocatedId(String mainSiteLocatedId) {
    this.mainSiteLocatedId = mainSiteLocatedId;
  }

  public Post mainSiteLocatedId(String mainSiteLocatedId) {
    this.mainSiteLocatedId = mainSiteLocatedId;
    return this;
  }

  public Set<String> getOtherSiteIds() {
    return otherSiteIds;
  }

  public void setOtherSiteIds(Set<String> otherSiteIds) {
    this.otherSiteIds = otherSiteIds;
  }

  public Post otherSiteIds(Set<String> otherSiteIds) {
    this.otherSiteIds = otherSiteIds;
    return this;
  }

  public String getEmployingBodyId() {
    return employingBodyId;
  }

  public void setEmployingBodyId(String employingBodyId) {
    this.employingBodyId = employingBodyId;
  }

  public Post employingBodyId(String employingBodyId) {
    this.employingBodyId = employingBodyId;
    return this;
  }

  public String getTrainingBodyId() {
    return trainingBodyId;
  }

  public void setTrainingBodyId(String trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
  }

  public Post trainingBodyId(String trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
    return this;
  }

  public String getApprovedGradeId() {
    return approvedGradeId;
  }

  public void setApprovedGradeId(String approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
  }

  public Post approvedGradeId(String approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
    return this;
  }

  public Set<String> getOtherGradeIds() {
    return otherGradeIds;
  }

  public void setOtherGradeIds(Set<String> otherGradeIds) {
    this.otherGradeIds = otherGradeIds;
  }

  public Post otherGradeIds(Set<String> otherGradeIds) {
    this.otherGradeIds = otherGradeIds;
    return this;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  public Post specialty(Specialty specialty) {
    this.specialty = specialty;
    return this;
  }

  public Set<Specialty> getOtherSpecialties() {
    return otherSpecialties;
  }

  public void setOtherSpecialties(Set<Specialty> otherSpecialties) {
    this.otherSpecialties = otherSpecialties;
  }

  public Post otherSpecialties(Set<Specialty> otherSpecialties) {
    this.otherSpecialties = otherSpecialties;
    return this;
  }

  public Specialty getSubspecialty() {
    return subspecialty;
  }

  public void setSubspecialty(Specialty subspecialty) {
    this.subspecialty = subspecialty;
  }

  public Post subspecialty(Specialty subspecialty) {
    this.subspecialty = subspecialty;
    return this;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public Post trainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
    return this;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public Post localPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
    return this;
  }

  public Set<Placement> getPlacementHistory() {
    return placementHistory;
  }

  public void setPlacementHistory(Set<Placement> placementHistory) {
    this.placementHistory = placementHistory;
  }

  public Post placementHistory(Set<Placement> placementHistory) {
    this.placementHistory = placementHistory;
    return this;
  }

  public Programme getProgrammes() {
    return programmes;
  }

  public void setProgrammes(Programme programmes) {
    this.programmes = programmes;
  }

  public Post programmes(Programme programmes) {
    this.programmes = programmes;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Post post = (Post) o;
    if (post.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Post{" +
        "id=" + id +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", status=" + status +
        ", suffix=" + suffix +
        ", managingLocalOffice='" + managingLocalOffice + '\'' +
        ", postFamily='" + postFamily + '\'' +
        ", oldPost=" + oldPost +
        ", newPost=" + newPost +
        ", mainSiteLocatedId='" + mainSiteLocatedId + '\'' +
        ", otherSiteIds=" + otherSiteIds +
        ", employingBodyId='" + employingBodyId + '\'' +
        ", trainingBodyId='" + trainingBodyId + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", otherGradeIds=" + otherGradeIds +
        ", specialty=" + specialty +
        ", otherSpecialties=" + otherSpecialties +
        ", subspecialty=" + subspecialty +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", placementHistory=" + placementHistory +
        ", programmes=" + programmes +
        '}';
  }
}
