package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class PlacementSavedEvent extends ApplicationEvent {

  private PlacementDTO placementDTO;

  public PlacementSavedEvent(PlacementDTO source) {
    super(source);
    this.placementDTO = source;
  }

  public PlacementDTO getPlacementDTO() {
    return placementDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementSavedEvent that = (PlacementSavedEvent) o;
    return Objects.equals(placementDTO, that.placementDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementDTO);
  }
}
