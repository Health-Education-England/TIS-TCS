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
import lombok.Data;

@Data
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
}
