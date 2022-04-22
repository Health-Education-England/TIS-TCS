package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class TokenResource {

  private static final String ENTITY_NAME = "token";
  private final Logger log = LoggerFactory.getLogger(TokenResource.class);

  @Autowired
  private TokenService tokenService;

  @PostMapping("/token")
  public ResponseEntity<String> getToken() throws URISyntaxException {
    String token = tokenService.createToken("classname"); // the prefix can be customised. Here we can analyze the url and set the entity/class name as a prefix.

    return ResponseEntity.created(new URI("/api/token/get/"))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, ""))
        .body(token);
  }

}
