package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;

/**
 * Entity of the view v_PostEsrLatestEvent.
 */
@Entity
@Immutable
@Table(name = "v_PostEsrLatestEvent")
@Data
public class PostEsrLatestEventView implements Serializable {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "postId")
  private Long postId;
  @Column(name = "eventDateTime")
  private LocalDateTime eventDateTime;
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
