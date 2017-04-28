package com.transformuk.hee.tis.config;

import com.transformuk.hee.tis.security.config.HeeProfileClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HeeProfileClientConfig.class)
public class RestClientConfig {
}
