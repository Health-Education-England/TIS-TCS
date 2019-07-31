package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class GmcDetailsSavedEvent extends ApplicationEvent {

  private GmcDetailsDTO gmcDetailsDTO;

  public GmcDetailsSavedEvent(@NonNull GmcDetailsDTO source) {
    super(source);
    this.gmcDetailsDTO = source;
  }

  public GmcDetailsDTO getGmcDetailsDTO() {
    return gmcDetailsDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GmcDetailsSavedEvent that = (GmcDetailsSavedEvent) o;
    return Objects.equals(gmcDetailsDTO, that.gmcDetailsDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gmcDetailsDTO);
  }
}
