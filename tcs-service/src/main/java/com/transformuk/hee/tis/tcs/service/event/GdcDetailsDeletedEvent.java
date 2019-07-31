package com.transformuk.hee.tis.tcs.service.event;

import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class GdcDetailsDeletedEvent extends ApplicationEvent {

  private Long gdcDetailsId;

  public GdcDetailsDeletedEvent(@NonNull Long gdcDetailsId) {
    super(gdcDetailsId);
    this.gdcDetailsId = gdcDetailsId;
  }

  public Long getGdcDetailsId() {
    return gdcDetailsId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdcDetailsDeletedEvent that = (GdcDetailsDeletedEvent) o;
    return Objects.equals(gdcDetailsId, that.gdcDetailsId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gdcDetailsId);
  }
}
