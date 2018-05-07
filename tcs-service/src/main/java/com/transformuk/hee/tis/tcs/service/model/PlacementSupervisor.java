package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "PlacementSupervisor")
public class PlacementSupervisor implements Serializable {
    @EmbeddedId
    private PlacementSupervisorId id;

    public PlacementSupervisor() {
    }

    public PlacementSupervisor(PlacementSupervisorId id) {
        this.id = id;
    }

    public PlacementSupervisor(final Long placementId, final Long personId, final Integer type) {
        this.id = new PlacementSupervisorId(placementId, personId, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacementSupervisor that = (PlacementSupervisor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public PlacementSupervisorId getId() {
        return id;
    }

    public void setId(PlacementSupervisorId id) {
        this.id = id;
    }
}
