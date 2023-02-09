package com.vmware.sfdc.demo.accountservice.webclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.force.api.ApiSession;

import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    @Autowired
    private WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceClient.class);

    public Mono<ApiSession> getApiSession() {
        LOGGER.debug("getApiSession()");
        return this.webClient.get().uri("/login")
                .retrieve()
                .bodyToMono(ApiSession.class);
    }
}
