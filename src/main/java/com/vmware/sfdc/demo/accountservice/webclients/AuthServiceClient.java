package com.vmware.sfdc.demo.accountservice.webclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.force.api.ApiSession;
import com.vmware.sfdc.demo.accountservice.configuration.ApiProperties;

import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {
    private final WebClient webClient;
    private final ApiProperties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceClient.class);

    public AuthServiceClient(ApiProperties properties) {
        this.properties = properties;
        LOGGER.debug(String.format("AuthServiceClient: properties.getAuthserviceURL() {}",
                properties.getAuthserviceURL()));
        this.webClient = WebClient.create("http://localhost:52880/api");
    }

    public Mono<ApiSession> getApiSession() {
        LOGGER.debug("getApiSession()");
        return this.webClient.get().uri("/login")
                .retrieve()
                .bodyToMono(ApiSession.class);
    }
}
