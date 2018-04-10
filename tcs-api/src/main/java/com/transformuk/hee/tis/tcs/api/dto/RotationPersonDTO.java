package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RotationPerson entity.
 */
public class RotationPersonDTO implements Serializable {
    
    @Null(groups = Create.class, message = "Id must be null when creating a new rotation-person relationship")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    private Long id;
    
    @NotNull(message = "Person Id must not be null when updating rotation-person relationships")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    private Long personId;
    
    @NotNull(message = "Rotation Id must not be null when updating rotation-person relationships")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
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
