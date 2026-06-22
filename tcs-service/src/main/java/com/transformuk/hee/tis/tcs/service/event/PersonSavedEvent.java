package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class PersonSavedEvent extends ApplicationEvent {

  private final PersonDTO personDTO;
  private final PersonDTO previousPersonDTO;

  public PersonSavedEvent(@NonNull PersonDTO source) {
    this(null, source);
  }

  public PersonSavedEvent(@Nullable PersonDTO previousPersonDto, @NonNull PersonDTO source) {
    super(source);
    this.personDTO = source;
    this.previousPersonDTO = previousPersonDto;
  }

  public PersonDTO getPersonDto() {
    return personDTO;
  }

  public PersonDTO getPreviousPersonDto() {
    return previousPersonDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonSavedEvent that = (PersonSavedEvent) o;
    return Objects.equals(personDTO, that.personDTO)
        && Objects.equals(previousPersonDTO, that.previousPersonDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousPersonDTO, personDTO);
  }
}
