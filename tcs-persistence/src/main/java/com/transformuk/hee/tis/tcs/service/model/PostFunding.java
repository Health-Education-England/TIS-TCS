package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.Type;

/**
 * A PostFunding.
 */
@Data
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

  @Column(name = "fundingTypeId")
  private Long fundingTypeId;

  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "fundingSubTypeId")
  private UUID fundingSubTypeId;

  @Column(name = "info")
  private String info;

  @Column(name = "startDate")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  @Column(name = "fundingBodyId")
  private String fundingBodyId;

  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "fundingReasonId")
  private UUID fundingReasonId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFunding that = (PostFunding) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(fundingType, that.fundingType) &&
        Objects.equals(fundingTypeId, that.fundingTypeId) &&
        Objects.equals(info, that.info) &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDate, that.endDate) &&
        Objects.equals(fundingBodyId, that.fundingBodyId) &&
        Objects.equals(fundingReasonId, that.fundingReasonId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, intrepidId, fundingType, fundingTypeId, info, startDate, endDate,
        fundingBodyId, fundingReasonId);
  }
}
