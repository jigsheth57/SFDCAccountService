package com.vmware.sfdc.demo.accountservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.force.api.ApiSession;

import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    private final WebClient client;

    @Value("${AUTHSERVICE_URL}")
    private String authserviceURL;

    public AuthServiceClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(authserviceURL).build();
    }

    public Mono<ApiSession> getStatus() {
        return this.client.get().uri("/login").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ApiSession.class);
    }

}
