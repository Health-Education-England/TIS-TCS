package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PlacementSupervisor")
public class PlacementSupervisor implements Serializable {

  @EmbeddedId
  private PlacementSupervisorId id;

  public PlacementSupervisor(final Long placementId, final Long personId, final Integer type) {
    this.id = new PlacementSupervisorId(placementId, personId, type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSupervisor that = (PlacementSupervisor) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }
}
