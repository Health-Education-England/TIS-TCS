package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;

/**
 * This DTO is used in the post list for ESR ETL responses, it's meant to keep only minimal that
 * needed for ESR processing. Be aware of using it in other places.
 */
public class PostEsrDTO implements Serializable {

  private static final long serialVersionUID = -7836260723895355083L;

  private Long id;

  private String nationalPostNumber;

  private Status status;

  private String owner;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

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

  @Override
  public String toString() {
    return "PostEsrDTO{" +
        "id=" + id +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", status=" + status +
        ", owner='" + owner + '\'' +
        '}';
  }
}

