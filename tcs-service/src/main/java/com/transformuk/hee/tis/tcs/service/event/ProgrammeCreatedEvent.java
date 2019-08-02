package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class ProgrammeCreatedEvent extends ApplicationEvent {

  private ProgrammeDTO programmeDTO;

  public ProgrammeCreatedEvent(ProgrammeDTO source) {
    super(source);
    this.programmeDTO = source;
  }

  public ProgrammeDTO getProgrammeDTO() {
    return programmeDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeCreatedEvent that = (ProgrammeCreatedEvent) o;
    return Objects.equals(programmeDTO, that.programmeDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeDTO);
  }
}
