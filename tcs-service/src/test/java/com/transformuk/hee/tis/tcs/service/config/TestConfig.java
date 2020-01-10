package com.transformuk.hee.tis.tcs.service.config;

import java.time.Instant;
import java.util.List;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
  @Bean
  public AuditEventRepository auditEventRepository() {
    return new AuditEventRepository() {
      @Override
      public void add(AuditEvent event) {

      }

      @Override
      public List<AuditEvent> find(String principal, Instant after,
          String type) {
        return null;
      }
    };
  }
}
