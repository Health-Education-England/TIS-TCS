package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class GmcDetailsDeletedEvent extends ApplicationEvent {

  private Long gmcDetailsId;

  public GmcDetailsDeletedEvent(@NonNull Long gmcDetailsId) {
    super(gmcDetailsId);
    this.gmcDetailsId = gmcDetailsId;
  }

  public Long getGmcDetailsId() {
    return gmcDetailsId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GmcDetailsDeletedEvent that = (GmcDetailsDeletedEvent) o;
    return Objects.equals(gmcDetailsId, that.gmcDetailsId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gmcDetailsId);
  }
}
