package com.transformuk.hee.tis.tcs.service.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository{

  @Autowired
  private RedisTemplate redisTemplate;

  public Object get(final String key) {
    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    return operations.get(key);
  }

  public void set(final String key, Object value) {
    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    operations.set(key, value);
  }

  public void setWithExpiryLimit(final String key, Object value, Long expireTime) {
    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    operations.set(key, value);
    redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
  }

  public boolean exists(final String key) {
    return redisTemplate.hasKey(key);
  }

  public boolean remove(final String key) {
    if (exists(key)) {
      return redisTemplate.delete(key);
    }
    return false;
  }
}
