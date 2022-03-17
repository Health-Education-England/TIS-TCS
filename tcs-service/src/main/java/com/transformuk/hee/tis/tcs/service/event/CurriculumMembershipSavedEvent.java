package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

public class CurriculumMembershipSavedEvent extends ApplicationEvent {

  private ProgrammeMembershipDTO programmeMembershipDTO;

  public CurriculumMembershipSavedEvent(ProgrammeMembershipDTO source) {
    super(source);
    this.programmeMembershipDTO = source;
  }

  public ProgrammeMembershipDTO getProgrammeMembershipDTO() {
    return programmeMembershipDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurriculumMembershipSavedEvent that = (CurriculumMembershipSavedEvent) o;
    return Objects.equals(programmeMembershipDTO, that.programmeMembershipDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeMembershipDTO);
  }
}
