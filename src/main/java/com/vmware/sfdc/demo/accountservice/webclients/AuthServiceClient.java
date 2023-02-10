package com.vmware.sfdc.demo.accountservice.webclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.force.api.ApiSession;

import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    @Autowired
    private WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceClient.class);

    public Mono<ApiSession> getApiSession(HttpHeaders headers) {
        LOGGER.debug("getApiSession()");
        return this.webClient.get().uri("/login")
                .header(headers.containsKey("Authorization") ? "Authorization" : "Cookie",
                        headers.containsKey("Authorization") ? headers.get("Authorization").get(0)
                                : headers.containsKey("Cookie") ? headers.get("Cookie").get(0) : "")
                .header("X-B3-TraceId", headers.containsKey("X-B3-TraceId") ? headers.get("X-B3-TraceId").get(0) : "")
                .retrieve()
                .bodyToMono(ApiSession.class);
    }
}
