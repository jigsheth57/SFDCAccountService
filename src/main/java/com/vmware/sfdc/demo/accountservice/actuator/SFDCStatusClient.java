package com.vmware.sfdc.demo.accountservice.actuator;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.vmware.sfdc.demo.accountservice.SFDCConstant;

import reactor.core.publisher.Mono;

@Component
public class SFDCStatusClient {

    private final WebClient client;

    public SFDCStatusClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(SFDCConstant.STATUS_URL).build();
    }

    public Mono<SFDCStatus> getStatus() {
        return this.client.get().accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SFDCStatus.class);
    }
}
