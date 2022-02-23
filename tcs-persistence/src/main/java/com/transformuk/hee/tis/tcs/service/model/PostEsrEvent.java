package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 * A list of post events that are linked to ESR
 * <p>
 * This is a list of matched, unmatched (TODO: or deleted?) events
 */
@Entity
@Table(name = "PostEsrEvent")
@Data
public class PostEsrEvent implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "postId")
  private Post post;

  @Column(name = "eventDateTime")
  private Date eventDateTime;
  @Column(name = "filename")
  private String filename;
  @Column(name = "positionNumber")
  private Long positionNumber;
  @Column(name = "positionId")
  private Long positionId;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostEsrEventStatus status;
}
