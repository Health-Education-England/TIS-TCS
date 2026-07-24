package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class PlacementDeletedEvent extends ApplicationEvent {

  private final PlacementDTO placementDto;

  public PlacementDeletedEvent(PlacementDTO placementDto) {
    super(placementDto);
    this.placementDto = placementDto;
  }

  public PlacementDTO getPlacementDto() {
    return placementDto;
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
    return Objects.equals(placementDto, that.placementDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementDto);
  }
}
