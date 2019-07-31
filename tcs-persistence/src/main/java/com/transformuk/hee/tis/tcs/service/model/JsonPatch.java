package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
public class JsonPatch implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String tableDtoName;

  private String patchId;

  private String patch;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateAdded;

  private Boolean enabled;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTableDtoName() {
    return tableDtoName;
  }

  public void setTableDtoName(String tableDtoName) {
    this.tableDtoName = tableDtoName;
  }

  public String getPatchId() {
    return patchId;
  }

  public void setPatchId(String patchId) {
    this.patchId = patchId;
  }

  public String getPatch() {
    return patch;
  }

  public void setPatch(String patch) {
    this.patch = patch;
  }

  public Date getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(Date dateAdded) {
    this.dateAdded = dateAdded;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public JsonPatch tableDtoName(String tableDtoName) {
    this.tableDtoName = tableDtoName;
    return this;
  }

  public JsonPatch patchId(String patchId) {
    this.patchId = patchId;
    return this;
  }

  public JsonPatch patch(String patch) {
    this.patch = patch;
    return this;
  }

  public JsonPatch enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public JsonPatch dateAdded(Date dateAdded) {
    this.dateAdded = dateAdded;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonPatch jsonPatch = (JsonPatch) o;
    if (jsonPatch.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, jsonPatch.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "JsonPatch{" +
        "id=" + id +
        ", tableDtoName='" + tableDtoName + '\'' +
        ", patchId='" + patchId + '\'' +
        ", patch='" + patch + '\'' +
        ", dateAdded=" + dateAdded +
        ", enabled=" + enabled +
        '}';
  }
}
