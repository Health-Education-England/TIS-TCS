package com.transformuk.hee.tis.tcs.service.config;

import com.transformuk.hee.tis.tcs.service.service.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
//@EnableElastiCache({@CacheClusterConfig(name = "yafang-test")}) // this is the config for using aws elasticache Redis cluster
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private Integer port;

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean(value = "redisTemplate")
  public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate redisTemplate = new RedisTemplate<>();
    // If below 2 lines are not set, the keys and values will have a magic prefix like "\xac\xed\x00\x05t\x00\x03bbb"
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    return redisTemplate;
  }

//  @Bean(name = "cacheManager")
//  public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
//    return RedisCacheManager.builder(jedisConnectionFactory)
//        .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
//        .build();
//  }
}
