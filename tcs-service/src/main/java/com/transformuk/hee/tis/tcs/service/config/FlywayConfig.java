package com.transformuk.hee.tis.tcs.service.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration
 */
@Configuration
public class FlywayConfig {

  @Value("${spring.flyway.url}")
  private String url;

  @Value("${spring.flyway.password}")
  private String password;

  @Value("${spring.flyway.user}")
  private String user;

  @Value("${spring.flyway.locations}")
  private String migrationFilesLocations;

  @Value("${spring.flyway.schemas}")
  private String schemas;

  @Value("${spring.flyway.baseline-on-migrate}")
  private boolean baseLineOnMigrate;

  @Value("${spring.flyway.clean-on-validation-error}")
  private boolean cleanOnValidationError;

  @Value("${spring.flyway.out-of-order}")
  private boolean outOfOrder;

  // Since Spring Boot 2 brings
  @Bean(initMethod = "migrate")
  Flyway flyway() {
    Flyway flyway = new Flyway();
    flyway.setBaselineOnMigrate(baseLineOnMigrate);
    flyway.setLocations(migrationFilesLocations);
    flyway.setDataSource(url, user, password);
    flyway.setCleanOnValidationError(cleanOnValidationError);
    flyway.setOutOfOrder(outOfOrder);
    flyway.info();
    return flyway;
  }
}
