package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Rotation entity.
 */
public class RotationDTO implements Serializable {
    
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Create.class, message = "Id must be null when creating a new rotation")
    private Long id;
    
    @NotNull(groups = Create.class, message = "Programme id must not be null when creating a new rotation")
    private Long programmeId;
    
    @Null(groups = Create.class, message = "Programme name must be null when creating a new rotation")
    private String programmeName;
    
    @Null(groups = Create.class, message = "Programme number must be null when creating a new rotation")
    private String programmeNumber;
    
    @NotNull(groups = Create.class, message = "Rotation name must not be null when creating a new rotation")
    private String name;
    
    @NotNull(message = "Status is required", groups = {Update.class, Create.class})
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public String getProgrammeNumber() {
        return programmeNumber;
    }
    
    public void setProgrammeNumber(String programmeNumber) {
        this.programmeNumber = programmeNumber;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RotationDTO that = (RotationDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(programmeId, that.programmeId) &&
                Objects.equals(programmeName, that.programmeName) &&
                Objects.equals(programmeNumber, that.programmeNumber) &&
                Objects.equals(name, that.name) &&
                Objects.equals(status, that.status);
    }
    
    @Override
    public String toString() {
        return "RotationDTO{" +
                "id=" + id +
                ", programmeId=" + programmeId +
                ", programmeName='" + programmeName + '\'' +
                ", programmeNumber='" + programmeNumber + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
