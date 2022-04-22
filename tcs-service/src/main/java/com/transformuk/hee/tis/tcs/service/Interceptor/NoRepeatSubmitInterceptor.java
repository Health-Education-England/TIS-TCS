package com.transformuk.hee.tis.tcs.service.Interceptor;

import com.transformuk.hee.tis.tcs.service.annotation.NoRepeatSubmit;
import com.transformuk.hee.tis.tcs.service.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class NoRepeatSubmitInterceptor implements HandlerInterceptor {
  @Autowired
  private TokenService tokenService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if ( !(handler instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();

    NoRepeatSubmit methodAnnotation = method.getAnnotation(NoRepeatSubmit.class);
    if (methodAnnotation != null) {
      try {
        return tokenService.checkToken(request);
      } catch (Exception ex) {
        response.sendError(409, ex.getMessage());
        return false;
      }
    }
    return true;
  }
}
