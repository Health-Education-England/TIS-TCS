package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.beans.Transient;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RotationPost entity.
 */
public class RotationPostDTO implements Serializable {
    
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Create.class, message = "Id must be null when creating a new rotation-post relationship")
    private Long id;
    
    @NotNull(message = "Post Id must not be null when updating rotation-post relationships")
    @DecimalMin(value = "0", groups = Update.class, message = "Post Id must not be negative")
    private Long postId;
    
    @NotNull(message = "Rotation Id must not be null when updating rotation-post relationships")
    @DecimalMin(value = "0", groups = Update.class, message = "Rotation Id must not be negative")
    private Long rotationId;
    
    @Null(groups = Update.class, message = "Programme Id must be null when updating rotation-post relationships")
    private Long programmeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getRotationId() {
        return rotationId;
    }

    public void setRotationId(Long rotationId) {
        this.rotationId = rotationId;
    }
    
    public Long getProgrammeId() {
        return programmeId;
    }
    
    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RotationPostDTO rotationPostDTO = (RotationPostDTO) o;
        if(rotationPostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rotationPostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
    
    @Override
    public String toString() {
        return "RotationPostDTO{" +
                "id=" + id +
                ", postId=" + postId +
                ", rotationId=" + rotationId +
                ", programmeId=" + programmeId +
                '}';
    }
}
