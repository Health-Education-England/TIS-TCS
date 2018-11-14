package com.transformuk.hee.tis.tcs.service.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig extends CachingConfigurerSupport {

  @Value("${cache.maxSize}")
  private int cacheMaxSize;

  @Value("${cache.timeToLiveInMinutes}")
  private int cacheTimeToLiveInMinutes;

  @Override
  @Bean
  public CacheManager cacheManager() {
    GuavaCacheManager cacheManager = new GuavaCacheManager();
    CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
        .maximumSize(cacheMaxSize)
        .expireAfterWrite(cacheTimeToLiveInMinutes, TimeUnit.MINUTES);
    cacheManager.setCacheBuilder(cacheBuilder);
    return cacheManager;
  }

}
