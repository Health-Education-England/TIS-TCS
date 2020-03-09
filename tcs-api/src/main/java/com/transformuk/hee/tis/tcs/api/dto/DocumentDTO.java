package com.transformuk.hee.tis.tcs.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.transformuk.hee.tis.tcs.api.dto.jackson.LocalDateTimeDeserializer;
import com.transformuk.hee.tis.tcs.api.dto.jackson.LocalDateTimeSerializer;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class DocumentDTO implements Serializable {

  private static final long serialVersionUID = -204651480188503498L;

  @NotNull(groups = Update.class, message = "Id must not be null when updating a document")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new document")
  private Long id;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime addedDate;
  @JsonIgnore
  private LocalDateTime amendedDate;
  @JsonIgnore
  private LocalDateTime inactiveDate;
  private String uploadedBy;
  @NotNull(groups = Update.class, message = "Title must not be null when updating a document")
  private String title;
  @Null(groups = Update.class, message = "Filename must be null when creating a updating document")
  private String fileName;
  @Null(groups = Update.class, message = "File Extension must be null when creating a updating document")
  private String fileExtension;
  @Null(groups = Update.class, message = "Content Type must be null when creating a updating document")
  private String contentType;
  @Null(groups = Update.class, message = "Size must be null when creating a updating document")
  private Long size;
  @NotNull(groups = Update.class, message = "Person ID must not be null when updating a document")
  private Long personId;
  @NotNull(groups = Update.class, message = "Status must not be null when updating a document")
  private Status status;
  @NotNull(groups = Update.class, message = "Version must not be null when updating a document")
  private Integer version;
  @JsonIgnore
  private byte[] bytes;
  private Set<TagDTO> tags;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final DocumentDTO that = (DocumentDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "DocumentDTO{" +
        "id=" + id +
        ", addedDate=" + addedDate +
        ", amendedDate=" + amendedDate +
        ", inactiveDate=" + inactiveDate +
        ", uploadedBy='" + uploadedBy + '\'' +
        ", title='" + title + '\'' +
        ", fileName='" + fileName + '\'' +
        ", fileExtension='" + fileExtension + '\'' +
        ", contentType='" + contentType + '\'' +
        ", size=" + size +
        ", personId=" + personId +
        ", status=" + status +
        ", version=" + version +
        ", tags=" + tags +
        '}';
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(final LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(final LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  public LocalDateTime getInactiveDate() {
    return inactiveDate;
  }

  public void setInactiveDate(final LocalDateTime inactiveDate) {
    this.inactiveDate = inactiveDate;
  }

  public String getUploadedBy() {
    return uploadedBy;
  }

  public void setUploadedBy(final String uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileExtension(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(final Long size) {
    this.size = size;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(final Long personId) {
    this.personId = personId;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(final Integer version) {
    this.version = version;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(final byte[] bytes) {
    this.bytes = bytes;
  }

  public Set<TagDTO> getTags() {
    return tags;
  }

  public void setTags(final Set<TagDTO> tags) {
    this.tags = tags;
  }
}
