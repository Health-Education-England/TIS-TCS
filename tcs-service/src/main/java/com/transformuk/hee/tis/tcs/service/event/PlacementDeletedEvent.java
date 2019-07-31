package com.transformuk.hee.tis.tcs.service.event;

import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class PlacementDeletedEvent extends ApplicationEvent {

  private Long placementId;
  private Long personId;

  public PlacementDeletedEvent(Long placementId, Long personId) {
    super(placementId);
    this.placementId = placementId;
    this.personId = personId;
  }

  public Long getPlacementId() {
    return placementId;
  }

  public Long getPersonId() {
    return personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementDeletedEvent that = (PlacementDeletedEvent) o;
    return Objects.equals(placementId, that.placementId) &&
        Objects.equals(personId, that.personId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementId, personId);
  }
}
