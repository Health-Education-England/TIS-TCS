package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

public class PersonCreatedEvent extends ApplicationEvent {

  private PersonDTO personDTO;

  public PersonCreatedEvent(@NonNull PersonDTO source) {
    super(source);
    this.personDTO = source;
  }

  public PersonDTO getPersonDTO() {
    return personDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonCreatedEvent that = (PersonCreatedEvent) o;
    return Objects.equals(personDTO, that.personDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personDTO);
  }
}
