package com.transformuk.hee.tis.tcs.service.config;

import java.time.Duration;
import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduledConfig {

  @Bean
  public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider) {
    return ScheduledLockConfigurationBuilder
        .withLockProvider(lockProvider)
        .withPoolSize(10)
        .withDefaultLockAtMostFor(Duration.ofMinutes(10))
        .build();
  }

  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }
}
