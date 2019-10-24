package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Programme implements Serializable {

  private static final long serialVersionUID = -8397864247495872712L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String intrepidId;
  @Enumerated(EnumType.STRING)
  private Status status;
  private String owner;
  private String programmeName;
  private String programmeNumber;
  
  @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL)
  private Set<ProgrammeCurriculum> curricula = new HashSet<>();

  @ManyToMany(mappedBy = "programmes")
  private Set<Post> posts;

  public Set<ProgrammeCurriculum> getCurricula() {
    return curricula;
  }

  public void setCurricula(final Set<ProgrammeCurriculum> curricula) {
    this.curricula = curricula;
  }

  public Programme curricula(final Set<ProgrammeCurriculum> curricula) {
    this.curricula = curricula;
    return this;
  }

  public Programme addCurriculum(final ProgrammeCurriculum curriculum) {
    this.curricula.add(curriculum);
    return this;
  }

  public Programme removeCurriculum(final ProgrammeCurriculum curriculum) {
    this.curricula.remove(curriculum);
    return this;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Programme intrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  public Programme status(final Status status) {
    this.status = status;
    return this;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(final String owner) {
    this.owner = owner;
  }

  public Programme owner(final String owner) {
    this.owner = owner;
    return this;
  }

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(final String programmeName) {
    this.programmeName = programmeName;
  }

  public Programme programmeName(final String programmeName) {
    this.programmeName = programmeName;
    return this;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(final String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }

  public Programme programmeNumber(final String programmeNumber) {
    this.programmeNumber = programmeNumber;
    return this;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(final Set<Post> posts) {
    this.posts = posts;
  }

  public Programme posts(final Set<Post> posts) {
    this.posts = posts;
    return this;
  }

  public Programme addPost(final Post post) {
    this.posts.add(post);
    return this;
  }

  public Programme removePost(final Post post) {
    this.posts.remove(post);
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Programme programme = (Programme) o;

    if (id != null ? !id.equals(programme.id) : programme.id != null) {
      return false;
    }
    if (intrepidId != null ? !intrepidId.equals(programme.intrepidId)
        : programme.intrepidId != null) {
      return false;
    }
    if (status != programme.status) {
      return false;
    }
    if (owner != null ? !owner.equals(programme.owner) : programme.owner != null) {
      return false;
    }
    if (programmeName != null ? !programmeName.equals(programme.programmeName)
        : programme.programmeName != null) {
      return false;
    }
    return programmeNumber != null ? programmeNumber.equals(programme.programmeNumber)
        : programme.programmeNumber == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + (programmeNumber != null ? programmeNumber.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Programme{" +
        "id=" + id +
        ", intrepidId=" + intrepidId +
        ", status='" + status + "'" +
        ", owner='" + owner + "'" +
        ", programmeName='" + programmeName + "'" +
        ", programmeNumber='" + programmeNumber + "'" +
        '}';
  }
}
