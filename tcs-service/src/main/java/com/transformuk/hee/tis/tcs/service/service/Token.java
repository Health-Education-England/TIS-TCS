package com.transformuk.hee.tis.tcs.service.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Token {
  private String uuid;
  private String className;
  private LocalDateTime createdDateTime;
}
