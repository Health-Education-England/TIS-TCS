package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PostFunding.
 */
@Entity
public class PostFunding implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "intrepidId")
  private String intrepidId;

  @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  private Post post;

  @Column(name = "fundingType")
  private String fundingType;

  @Column(name = "info")
  private String info;

  @Column(name = "startDate")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  @Column(name = "fundingBodyId")
  private String fundingBodyId;

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

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getFundingBodyId() {return fundingBodyId; }

  public void setFundingBodyId(String fundingBodyId) {this.fundingBodyId = fundingBodyId; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostFunding that = (PostFunding) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(fundingType, that.fundingType) &&
        Objects.equals(info, that.info) &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDate, that.endDate) &&
        Objects.equals(fundingBodyId, that.fundingBodyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, intrepidId, fundingType, info, startDate, endDate, fundingBodyId);
  }

  @Override
  public String toString() {
    return "PostFunding{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", fundingType='" + fundingType + '\'' +
        ", info='" + info + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", fundingBodyId='" + fundingBodyId + '\'' +
        '}';
  }
}
