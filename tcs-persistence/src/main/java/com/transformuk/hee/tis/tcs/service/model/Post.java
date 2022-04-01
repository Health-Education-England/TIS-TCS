package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Post")
public class Post implements Serializable {

  private static final long serialVersionUID = -4633307411677022598L;

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
  //PostSuffixConverter converts the value
  @Column(name = "suffix")
  private PostSuffix suffix;
  @Column(name = "owner")
  private String owner;
  @Column(name = "postFamily")
  private String postFamily;
  // Entity Trust defined in the Reference service
  @Column(name = "employingBodyId")
  private Long employingBodyId;
  // Entity Trust defined in the Reference service
  @Column(name = "trainingBodyId")
  private Long trainingBodyId;
  @Column(name = "trainingDescription")
  private String trainingDescription;
  @Column(name = "localPostNumber")
  private String localPostNumber;
  @Column(name = "legacy")
  private boolean legacy;
  @Column(name = "bypassNPNGeneration")
  private boolean bypassNPNGeneration;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "oldPostId")
  private Post oldPost;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "newPostId")
  private Post newPost;
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "ProgrammePost",
      joinColumns = @JoinColumn(name = "postId"),
      inverseJoinColumns = @JoinColumn(name = "programmeId")
  )
  private Set<Programme> programmes = new HashSet<>();
  // Entity Site defined in the Reference service
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostSite> sites = new HashSet<>();
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostGrade> grades = new HashSet<>();
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostSpecialty> specialties = new HashSet<>();
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<PostFunding> fundings = new HashSet<>();
  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
  private Set<Placement> placementHistory = new HashSet<>();
  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
  private Set<PostTrust> associatedTrusts;
  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<PostEsrEvent> postEsrEvents;

  public Post intrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Post nationalPostNumber(final String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
    return this;
  }

  public Post status(final Status status) {
    this.status = status;
    return this;
  }

  public Post suffix(final PostSuffix suffix) {
    this.suffix = suffix;
    return this;
  }

  public Post owner(final String owner) {
    this.owner = owner;
    return this;
  }

  public Post postFamily(final String postFamily) {
    this.postFamily = postFamily;
    return this;
  }

  public Post oldPost(final Post oldPost) {
    this.oldPost = oldPost;
    return this;
  }

  public Post newPost(final Post newPost) {
    this.newPost = newPost;
    return this;
  }

  public Post employingBodyId(final Long employingBodyId) {
    this.employingBodyId = employingBodyId;
    return this;
  }

  public Post trainingBodyId(final Long trainingBodyId) {
    this.trainingBodyId = trainingBodyId;
    return this;
  }

  public Post trainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
    return this;
  }

  public Post localPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
    return this;
  }

  public Post placementHistory(final Set<Placement> placementHistory) {
    this.placementHistory = placementHistory;
    return this;
  }

  public Post programmes(final Set<Programme> programmes) {
    this.programmes = programmes;
    return this;
  }

  public Post addProgramme(final Programme programme) {
    this.programmes.add(programme);
    return this;
  }

  public Post removeProgramme(final Programme programme) {
    this.programmes.remove(programme);
    return this;
  }

  public void setSites(final Set<PostSite> sites) {
    this.sites.clear();
    this.sites.addAll(sites);
  }

  public Post sites(final Set<PostSite> sites) {
    this.sites = sites;
    return this;
  }

  public void setGrades(final Set<PostGrade> grades) {
    this.grades.clear();
    this.grades.addAll(grades);
  }

  public Post grades(final Set<PostGrade> grades) {
    this.grades = grades;
    return this;
  }

  public void setSpecialties(final Set<PostSpecialty> specialties) {
    this.specialties.clear();
    this.specialties.addAll(specialties);
  }

  public Post specialties(final Set<PostSpecialty> specialties) {
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
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
