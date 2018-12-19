package com.transformuk.hee.tis.tcs.service.event;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SpecialtyDeletedEvent that = (SpecialtyDeletedEvent) o;
    return Objects.equals(specialtyId, that.specialtyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specialtyId);
  }
}
