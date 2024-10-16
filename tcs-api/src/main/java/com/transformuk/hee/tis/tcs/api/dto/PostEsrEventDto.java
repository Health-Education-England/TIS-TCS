package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * This DTO is used for ESR Reconcilation Event for Post.
 */
@Data
public class PostEsrEventDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private LocalDateTime eventDateTime;
  private String filename;
  private Long postId;
  private Long positionNumber;
  private Long positionId;
  private PostEsrEventStatus status;
}
