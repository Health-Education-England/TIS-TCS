package com.transformuk.hee.tis.tcs.service.event;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class ProgrammeDeletedEvent extends ApplicationEvent {

  private Long programmeId;

  public ProgrammeDeletedEvent(Long programmeId) {
    super(programmeId);
    this.programmeId = programmeId;
  }

  public Long getProgrammeId() {
    return programmeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeDeletedEvent that = (ProgrammeDeletedEvent) o;
    return Objects.equals(programmeId, that.programmeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeId);
  }
}
