package com.transformuk.hee.tis.tcs;

import com.transformuk.hee.tis.tcs.service.repository.PersonElasticSearchRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@TestConfiguration
public class TestConfig {

  @Bean
  public PersonElasticSearchService personElasticSearchService() {
    return Mockito.mock(PersonElasticSearchService.class);
  }

  @Bean
  public PersonElasticSearchRepository personElasticSearchRepository(){
    return Mockito.mock(PersonElasticSearchRepository.class);
  }

  @Bean
  public ElasticsearchTemplate elasticsearchTemplate(){
    return Mockito.mock(ElasticsearchTemplate.class);
  }

}
