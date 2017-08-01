package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PostFunding entity.
 */
public class PostFundingDTO implements Serializable {

  private Long id;

  private String fundingId;

  private String fundingComponentsId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFundingId() {
    return fundingId;
  }

  public void setFundingId(String fundingId) {
    this.fundingId = fundingId;
  }

  public String getFundingComponentsId() {
    return fundingComponentsId;
  }

  public void setFundingComponentsId(String fundingComponentsId) {
    this.fundingComponentsId = fundingComponentsId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostFundingDTO postFundingDTO = (PostFundingDTO) o;

    if (!Objects.equals(id, postFundingDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PostFundingDTO{" +
        "id=" + id +
        '}';
  }
}
