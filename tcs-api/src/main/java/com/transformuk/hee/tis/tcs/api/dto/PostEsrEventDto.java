package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostEsrEventStatus;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class PostEsrEventDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private Date exportedAt;
  private String filename;
  private Long postId;
  private Long positionNumber;
  private Long positionId;
  private PostEsrEventStatus status;
}
