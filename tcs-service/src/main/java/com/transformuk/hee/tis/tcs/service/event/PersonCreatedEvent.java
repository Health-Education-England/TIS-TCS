package com.transformuk.hee.tis.tcs.service.event;

import org.springframework.context.ApplicationEvent;

public class PersonCreatedEvent extends ApplicationEvent {

  private Long personId;

  public PersonCreatedEvent(Object source) {
    super(source);
  }

  public PersonCreatedEvent(Object source, Long personId) {
    this(source);
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }
}
