package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RotationPost entity.
 */
public class RotationPostDTO implements Serializable {

    private Long id;

    private Long postId;

    private Long rotationId;

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
            "id=" + getId() +
            ", postId=" + getPostId() +
            ", rotationId=" + getRotationId() +
            "}";
    }
}
