package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;

public class PlacementSavedEvent extends ApplicationEvent {

  private PlacementDTO placementDTO;
  private PlacementDTO previousPlacementDto;

  public PlacementSavedEvent(@Nullable PlacementDTO previousPlacementDto, PlacementDTO source) {
    super(source);
    this.placementDTO = source;
    this.previousPlacementDto = previousPlacementDto;
  }

  public PlacementDTO getPlacementDTO() {
    return placementDTO;
  }

  public PlacementDTO getPreviousPlacementDto() {
    return previousPlacementDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSavedEvent that = (PlacementSavedEvent) o;
    return Objects.equals(placementDTO, that.placementDTO)
        && Objects.equals(previousPlacementDto, that.previousPlacementDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementDTO, previousPlacementDto);
  }
}
