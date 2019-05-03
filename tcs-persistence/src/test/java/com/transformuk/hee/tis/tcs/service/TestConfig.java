package com.transformuk.hee.tis.tcs.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.transformuk.hee.tis.tcs.service.config.ApplicationProperties;

@ComponentScan(basePackages = {"com.transformuk.hee.tis.tcs.service"})
@EnableElasticsearchRepositories
@EnableAutoConfiguration()
@EnableConfigurationProperties({ApplicationProperties.class})
public class TestConfig {}
