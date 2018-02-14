package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel("Tag")
public class TagDTO {
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long id;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private LocalDateTime addedDate;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private LocalDateTime amendedDate;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long userId;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDateTime getAmendedDate() {
        return amendedDate;
    }

    public void setAmendedDate(LocalDateTime amendedDate) {
        this.amendedDate = amendedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}