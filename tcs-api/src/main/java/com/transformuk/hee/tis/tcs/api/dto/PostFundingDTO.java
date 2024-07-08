package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the PostFunding entity.
 */
@Data
public class PostFundingDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private String fundingType;

  private UUID fundingSubTypeId;

  private String info;

  @NotNull(message = "Post funding start date cannot be null or empty")
  private LocalDate startDate;

  private LocalDate endDate;

  private String fundingBodyId;

  private Long postId;

  private List<String> messageList = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFundingDTO that = (PostFundingDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(fundingType, that.fundingType) &&
        Objects.equals(fundingSubTypeId, that.fundingSubTypeId) &&
        Objects.equals(info, that.info) &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDate, that.endDate) &&
        Objects.equals(fundingBodyId, that.fundingBodyId) &&
        Objects.equals(messageList, that.messageList);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, fundingType, fundingSubTypeId, info, startDate, endDate,
            fundingBodyId, messageList);
  }
}
