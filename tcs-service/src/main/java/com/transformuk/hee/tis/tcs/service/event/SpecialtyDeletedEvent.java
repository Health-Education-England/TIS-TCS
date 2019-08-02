package com.transformuk.hee.tis.tcs.service.event;

import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class SpecialtyDeletedEvent extends ApplicationEvent {

  private Long specialtyId;

  public SpecialtyDeletedEvent(Long source) {
    super(source);
    this.specialtyId = source;
  }

  public Long getSpecialtyId() {
    return specialtyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialtyDeletedEvent that = (SpecialtyDeletedEvent) o;
    return Objects.equals(specialtyId, that.specialtyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specialtyId);
  }
}
