package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import lombok.Data;

/**
 * This DTO is used in the post list for ESR ETL responses, it's meant to keep only minimal that
 * needed for ESR processing. Be aware of using it in other places.
 */
@Data
public class PostEsrDTO implements Serializable {

  private static final long serialVersionUID = -7836260723895355083L;

  private Long id;

  private String nationalPostNumber;

  private Status status;

  private String owner;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostEsrDTO that = (PostEsrDTO) o;

    if (!id.equals(that.id)) {
      return false;
    }
    if (!nationalPostNumber.equals(that.nationalPostNumber)) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    return owner != null ? owner.equals(that.owner) : that.owner == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + nationalPostNumber.hashCode();
    result = 31 * result + status.hashCode();
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    return result;
  }
}

