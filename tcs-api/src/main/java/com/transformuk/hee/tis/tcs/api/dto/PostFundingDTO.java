package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the PostFunding entity.
 */
@Data
public class PostFundingDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private String fundingType;

  private String fundingSubType;

  private String info;

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
        Objects.equals(fundingSubType, that.fundingSubType) &&
        Objects.equals(info, that.info) &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDate, that.endDate) &&
        Objects.equals(fundingBodyId, that.fundingBodyId) &&
        Objects.equals(messageList, that.messageList);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, fundingType, fundingSubType, info, startDate, endDate, fundingBodyId, messageList);
  }
}
