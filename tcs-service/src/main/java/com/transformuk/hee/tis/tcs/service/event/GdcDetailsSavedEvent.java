package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class GdcDetailsSavedEvent extends ApplicationEvent {

  private GdcDetailsDTO gdcDetailsDTO;

  public GdcDetailsSavedEvent(@NonNull GdcDetailsDTO source) {
    super(source);
    this.gdcDetailsDTO = source;
  }

  public GdcDetailsDTO getGdcDetailsDTO() {
    return gdcDetailsDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GdcDetailsSavedEvent that = (GdcDetailsSavedEvent) o;
    return Objects.equals(gdcDetailsDTO, that.gdcDetailsDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gdcDetailsDTO);
  }
}
