package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SiteDTO implements Serializable {

  private Long id;
  private String name;
  private String siteKnownAs;
  private String siteNumber;
  private List<PostDTO> posts;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSiteKnownAs() {
    return siteKnownAs;
  }

  public void setSiteKnownAs(String siteKnownAs) {
    this.siteKnownAs = siteKnownAs;
  }

  public String getSiteNumber() {
    return siteNumber;
  }

  public void setSiteNumber(String siteNumber) {
    this.siteNumber = siteNumber;
  }

  public List<PostDTO> getPosts() {
    return posts;
  }

  public void setPosts(List<PostDTO> posts) {
    this.posts = posts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiteDTO siteDTO = (SiteDTO) o;
    return Objects.equals(id, siteDTO.id) &&
        Objects.equals(name, siteDTO.name) &&
        Objects.equals(siteKnownAs, siteDTO.siteKnownAs) &&
        Objects.equals(siteNumber, siteDTO.siteNumber) &&
        Objects.equals(posts, siteDTO.posts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, siteKnownAs, siteNumber, posts);
  }
}
