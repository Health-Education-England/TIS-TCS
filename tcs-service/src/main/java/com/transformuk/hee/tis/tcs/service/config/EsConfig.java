package com.transformuk.hee.tis.tcs.service.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

@Configuration
public class EsConfig {

  @Bean
  public Client client() throws Exception {
    Settings esSettings = Settings.builder()
        .put("cluster.name", "docker-cluster")
        .put("client.transport.sniff", false)
        .build();


    TransportClient client = new PreBuiltTransportClient(esSettings)
        .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    return client;
  }

  @Bean
  public ElasticsearchOperations elasticsearchTemplate() throws Exception {
    return new ElasticsearchTemplate(client());
  }
}
