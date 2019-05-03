package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlacementSupervisorId implements Serializable {
    @Column(name = "placementId")
    private Long placementId;

    @Column(name = "personId")
    private Long personId;

    @Column(name = "type")
    private Integer type;

    public PlacementSupervisorId() {
    }

    public PlacementSupervisorId(final Long placementId, final Long personId, final Integer type) {
        this.placementId = placementId;
        this.personId = personId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacementSupervisorId that = (PlacementSupervisorId) o;
        return Objects.equals(placementId, that.placementId) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(placementId, personId, type);
    }

    public Long getPlacementId() {
        return placementId;
    }

    public void setPlacementId(Long placementId) {
        this.placementId = placementId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
