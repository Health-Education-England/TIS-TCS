package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;

/**
 * An event triggered when a Programme is saved.
 */
public class ProgrammeSavedEvent extends ApplicationEvent {

  private final ProgrammeDTO programmeDTO;

  private final ProgrammeDTO previousProgrammeDto;

  /**
   * Constructor for ProgrammeSavedEvent with only the source programme.
   *
   * @param source the current programme that was saved
   */
  public ProgrammeSavedEvent(ProgrammeDTO source) {
    this(null, source);
  }

  /**
   * Constructor for ProgrammeSavedEvent with both the previous and current programme.
   *
   * @param previousProgrammeDto the previous state of the programme before the save operation
   * @param source the current programme that was saved
   */
  public ProgrammeSavedEvent(ProgrammeDTO previousProgrammeDto, ProgrammeDTO source) {
    super(source);
    this.programmeDTO = source;
    this.previousProgrammeDto = previousProgrammeDto;
  }

  public ProgrammeDTO getProgrammeDTO() {
    return programmeDTO;
  }

  public ProgrammeDTO getPreviousProgrammeDto() {
    return previousProgrammeDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeSavedEvent that = (ProgrammeSavedEvent) o;
    return Objects.equals(programmeDTO, that.programmeDTO)
        && Objects.equals(previousProgrammeDto, that.previousProgrammeDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeDTO,  previousProgrammeDto);
  }
}
