package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Rotation entity.
 */
public class RotationDTO implements Serializable {

    private Long id;

    private Long programmeId;
    
    private String programmeName;
    
    private String programmeNumber;

    private String name;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
