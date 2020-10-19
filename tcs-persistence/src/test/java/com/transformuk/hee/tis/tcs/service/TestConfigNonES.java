package com.transformuk.hee.tis.tcs.service;

import com.transformuk.hee.tis.tcs.service.config.ApplicationProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.transformuk.hee.tis.tcs.service"})
@EnableAutoConfiguration()
@EnableConfigurationProperties({ApplicationProperties.class})
public class TestConfigNonES {

}
