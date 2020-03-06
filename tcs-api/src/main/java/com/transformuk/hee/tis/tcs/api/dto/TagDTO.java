package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDTO {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a tag")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Update.class, message = "Id must be null when creating a new tag")
  private Long id;
  @JsonIgnore
  private LocalDateTime addedDate;
  @JsonIgnore
  private LocalDateTime amendedDate;
  private String name;

  public TagDTO(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public TagDTO(final String name) {
    this.name = name;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TagDTO tagDTO = (TagDTO) o;

    if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(tagDTO.name)) {
      return false;
    }

    return Objects.equals(id, tagDTO.id) || name.equalsIgnoreCase(tagDTO.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name.toLowerCase());
  }
}
