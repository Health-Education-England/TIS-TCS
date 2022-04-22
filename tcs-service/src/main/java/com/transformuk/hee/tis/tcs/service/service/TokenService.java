package com.transformuk.hee.tis.tcs.service.service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

  public String createToken(String className);

  public boolean checkToken(HttpServletRequest request) throws Exception;
}
