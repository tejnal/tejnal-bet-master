package com.tejnalbetmaster.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class SpringTestConfig {
  private RestTemplate template = new RestTemplate();

  @Bean
  public RestTemplate restTemplate() {
    return template;
  }
}
