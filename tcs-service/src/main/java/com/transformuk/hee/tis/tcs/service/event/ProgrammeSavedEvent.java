package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class ProgrammeSavedEvent extends ApplicationEvent {

  private ProgrammeDTO programmeDTO;

  public ProgrammeSavedEvent(ProgrammeDTO source) {
    super(source);
    this.programmeDTO = source;
  }

  public ProgrammeDTO getProgrammeDTO() {
    return programmeDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeSavedEvent that = (ProgrammeSavedEvent) o;
    return Objects.equals(programmeDTO, that.programmeDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeDTO);
  }
}
