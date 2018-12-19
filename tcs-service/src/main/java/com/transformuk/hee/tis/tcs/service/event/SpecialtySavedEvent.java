package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class SpecialtySavedEvent extends ApplicationEvent {

  private SpecialtyDTO specialtyDTO;

  public SpecialtySavedEvent(SpecialtyDTO source) {
    super(source);
    this.specialtyDTO = source;
  }

  public SpecialtyDTO getSpecialtyDTO() {
    return specialtyDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SpecialtySavedEvent that = (SpecialtySavedEvent) o;
    return Objects.equals(specialtyDTO, that.specialtyDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specialtyDTO);
  }
}
