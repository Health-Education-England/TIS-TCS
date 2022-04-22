package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.service.RedisRepository;
import com.transformuk.hee.tis.tcs.service.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

  @Autowired
  private RedisRepository redisRepository;

  @Override
  public String createToken(String className) {
    String uuid = UUID.randomUUID().toString();
    StringBuilder token = new StringBuilder();
    String delimiter = "_";
    token.append("TOKEN").append(delimiter).append(className).append(delimiter).append(uuid);
    redisRepository.setWithExpiryLimit(token.toString(), LocalDateTime.now().toString(), 1000000L);
//    boolean exists = redisRepository.exists(token.toString());
//    String str = (String)redisRepository.get(token.toString());
//    String str1 = (String)redisRepository.get("aaa");
    return token.toString();
  }

  @Override
  public boolean checkToken(HttpServletRequest request) throws Exception{
    String token = request.getHeader("PPS");
    if (StringUtils.isEmpty(token)) {
      throw new Exception("Token doesn't exists in the header");
    }
    if (!redisRepository.exists(token)) {
      throw new Exception("Repeated submission is not allowed");
    }
    boolean remove = redisRepository.remove(token);
    if (!remove) {
      throw new Exception("Removing token failed");
    }
    return true;
  }
}
