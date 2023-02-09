package com.vmware.sfdc.demo.accountservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveWebClientConfig {
    private final ApiProperties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveWebClientConfig.class);

    public ReactiveWebClientConfig(ApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient webClient() {
        LOGGER.debug(String.format("webClient: properties.getAuthserviceURL() {}", properties.getAuthserviceURL()));
        return WebClient.builder().baseUrl("http://localhost:52880/api").build();
    }
}
