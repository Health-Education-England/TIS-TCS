package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RotationPerson entity.
 */
public class RotationPersonDTO implements Serializable {

    private Long id;

    private Long personId;

    private Long rotationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
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

        RotationPersonDTO rotationPersonDTO = (RotationPersonDTO) o;
        if(rotationPersonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rotationPersonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RotationPersonDTO{" +
            "id=" + getId() +
            ", personId=" + getPersonId() +
            ", rotationId=" + getRotationId() +
            "}";
    }
}
