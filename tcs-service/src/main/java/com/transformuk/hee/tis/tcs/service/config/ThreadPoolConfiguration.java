package com.transformuk.hee.tis.tcs.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfiguration {

  private static final int EXECUTOR_THREAD_POOL_SIZE = 50;

  @Bean
  public DelegatingSecurityContextExecutorService executorService(){
    return new DelegatingSecurityContextExecutorService(Executors.newCachedThreadPool());

  }
}
