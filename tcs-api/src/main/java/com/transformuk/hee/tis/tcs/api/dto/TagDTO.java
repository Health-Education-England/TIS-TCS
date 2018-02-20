package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDTO(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return Objects.equals(id, tagDTO.id) || Objects.equals(name, tagDTO.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
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