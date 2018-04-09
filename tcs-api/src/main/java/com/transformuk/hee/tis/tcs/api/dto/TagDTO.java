package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel("Tag")
public class TagDTO {
    @NotNull(groups = Update.class, message = "Id must not be null when updating a tag")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Update.class, message = "Id must be null when creating a new tag")
    private Long id;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private LocalDateTime addedDate;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private LocalDateTime amendedDate;
    private String name;

    public TagDTO() {
    }

    public TagDTO(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public TagDTO(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", addedDate=" + addedDate +
                ", amendedDate=" + amendedDate +
                ", name='" + name + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}