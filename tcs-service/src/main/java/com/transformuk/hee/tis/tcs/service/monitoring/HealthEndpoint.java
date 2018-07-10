package com.transformuk.hee.tis.tcs.service.monitoring;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class HealthEndpoint implements Endpoint<Status> {

  @Override
  public String getId() {
    return "healthz";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isSensitive() {
    return true;
  }

  @Override
  public Status invoke() {
    return Status.UP;
  }
}
