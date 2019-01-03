package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class PersonSavedEvent extends ApplicationEvent {

  private PersonDTO personDTO;

  public PersonSavedEvent(@NonNull PersonDTO source) {
    super(source);
    this.personDTO = source;
  }

  public PersonDTO getPersonDTO() {
    return personDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonSavedEvent that = (PersonSavedEvent) o;
    return Objects.equals(personDTO, that.personDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personDTO);
  }
}
