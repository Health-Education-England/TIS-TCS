package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PlacementSupervisorId implements Serializable {

  @Column(name = "placementId")
  private Long placementId;

  @Column(name = "personId")
  private Long personId;

  @Column(name = "type")
  private Integer type;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSupervisorId that = (PlacementSupervisorId) o;
    return Objects.equals(placementId, that.placementId) &&
        Objects.equals(personId, that.personId) &&
        Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementId, personId, type);
  }
}
