package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class PersonDeletedEvent extends ApplicationEvent {

  private Long personId;

  public PersonDeletedEvent(@NonNull Long source) {
    super(source);
    this.personId = source;
  }

  public Long getPersonId() {
    return personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonDeletedEvent that = (PersonDeletedEvent) o;
    return Objects.equals(personId, that.personId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId);
  }
}
