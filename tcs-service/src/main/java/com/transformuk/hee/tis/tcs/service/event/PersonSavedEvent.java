package com.transformuk.hee.tis.tcs.service.event;

import org.springframework.context.ApplicationEvent;

public class PersonSavedEvent extends ApplicationEvent {

  private Long personId;

  public PersonSavedEvent(Object source) {
    super(source);
  }

  public PersonSavedEvent(Object source, Long personId) {
    this(source);
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }
}
