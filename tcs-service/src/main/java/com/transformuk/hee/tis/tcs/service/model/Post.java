package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Post.
 */
@Entity
@Table(name = "Post")
public class Post implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "intrepidId")
  private String intrepidId;

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

  // Entity Trust defined in the Reference service
  @Column(name = "employingBodyId")
  private String employingBodyId;

  // Entity Trust defined in the Reference service
  @Column(name = "trainingBodyId")
  private String trainingBodyId;

  @Column(name = "trainingDescription")
  private String trainingDescription;

  @Column(name = "localPostNumber")
  private String localPostNumber;

  @OneToOne
  @JoinColumn(name = "oldPostId")
  private Post oldPost;

  @OneToOne
  @JoinColumn(name = "newPostId")
  private Post newPost;

  @OneToOne
  @JoinColumn(name = "programmeId")
  private Programme programmes;

  // Entity Site defined in the Reference service
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<PostSite> sites = new HashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<PostGrade> grades = new HashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<PostSpecialty> specialties = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "PostPlacementHistory",
      joinColumns = @JoinColumn(name = "postId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "placementId", referencedColumnName = "id"))
  private Set<Placement> placementHistory = new HashSet<>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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

  public Set<PostSite> getSites() {
    return sites;
  }

  public void setSites(Set<PostSite> sites) {
    this.sites = sites;
  }

  public Post sites(Set<PostSite> sites) {
    this.sites = sites;
    return this;
  }

  public Set<PostGrade> getGrades() {
    return grades;
  }

  public void setGrades(Set<PostGrade> grades) {
    this.grades = grades;
  }

  public Post grades(Set<PostGrade> grades) {
    this.grades = grades;
    return this;
  }

  public Set<PostSpecialty> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<PostSpecialty> specialties) {
    this.specialties = specialties;
  }

  public Post specialties(Set<PostSpecialty> specialties) {
    this.specialties = specialties;
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
        ", intrepidId='" + intrepidId + '\'' +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", status=" + status +
        ", suffix=" + suffix +
        ", managingLocalOffice='" + managingLocalOffice + '\'' +
        ", postFamily='" + postFamily + '\'' +
        ", oldPost=" + oldPost +
        ", newPost=" + newPost +
        ", employingBodyId='" + employingBodyId + '\'' +
        ", trainingBodyId='" + trainingBodyId + '\'' +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", placementHistory=" + placementHistory +
        ", programmes=" + programmes +
        '}';
  }
}
