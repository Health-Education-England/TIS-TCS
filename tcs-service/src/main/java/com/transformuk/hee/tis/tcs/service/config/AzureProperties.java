package com.transformuk.hee.tis.tcs.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureProperties {

  @Value("${azure.accountName}")
  private String accountName;
  @Value("${azure.accountKey}")
  private String accountKey;
  @Value("${azure.containerName}")
  private String containerName;

  public String getAccountName() {
    return accountName;
  }

  public String getAccountKey() {
    return accountKey;
  }

  public String getContainerName() {
    return containerName;
  }
}
