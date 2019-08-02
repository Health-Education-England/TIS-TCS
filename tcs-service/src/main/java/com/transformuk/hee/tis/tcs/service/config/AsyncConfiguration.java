package com.transformuk.hee.tis.tcs.service.config;

import io.github.jhipster.config.JHipsterProperties;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

  private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

  private final JHipsterProperties jHipsterProperties;

  public AsyncConfiguration(JHipsterProperties jHipsterProperties) {
    this.jHipsterProperties = jHipsterProperties;
  }

  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(jHipsterProperties.getAsync().getCorePoolSize());
    executor.setMaxPoolSize(jHipsterProperties.getAsync().getMaxPoolSize());
    executor.setQueueCapacity(jHipsterProperties.getAsync().getQueueCapacity());
    executor.setThreadNamePrefix("tcs-Executor-");
    executor.initialize();
    return new DelegatingSecurityContextAsyncTaskExecutor(executor);

  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }

  /**
   * Bean to handle application events asynchronously
   *
   * @return
   */
  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
    SimpleApplicationEventMulticaster eventMulticaster
        = new SimpleApplicationEventMulticaster();

    eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
    return eventMulticaster;
  }
}
