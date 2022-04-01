package com.transformuk.hee.tis.tcs.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * Entity that links a Post record to a trust. This is used to filter out what Post records Trust
 * admin users can see
 */
@Data
@Entity
public class PostTrust {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "postId")
  @JsonBackReference
  private Post post;

  @Column(name = "trustId", nullable = false)
  private Long trustId;

  @Column(name = "trustCode")
  private String trustCode;

  @Column(name = "trustName")
  private String trustName;
}
