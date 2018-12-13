package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class ProgrammeMembershipDeletedEvent extends ApplicationEvent {

  private ProgrammeMembershipDTO programmeMembershipDTO;

  public ProgrammeMembershipDeletedEvent(ProgrammeMembershipDTO source) {
    super(source);
    this.programmeMembershipDTO = source;
  }

  public ProgrammeMembershipDTO getProgrammeMembershipDTO() {
    return programmeMembershipDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProgrammeMembershipDeletedEvent that = (ProgrammeMembershipDeletedEvent) o;
    return Objects.equals(programmeMembershipDTO, that.programmeMembershipDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeMembershipDTO);
  }
}
