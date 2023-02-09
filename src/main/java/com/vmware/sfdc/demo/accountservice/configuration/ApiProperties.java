package com.vmware.sfdc.demo.accountservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "sfdc")
public class ApiProperties {

    private String authserviceURL;
    private String query_contactByAccounts;
    private String query_opportunityByAccounts;

    public ApiProperties() {
        this.query_contactByAccounts = "select account.id, account.name, account.type, (select contact.id, contact.lastname from account.contacts) from account";
        this.query_opportunityByAccounts = "select account.id, account.name, account.type, (select opportunity.id, opportunity.name, opportunity.type, opportunity.amount, opportunity.stagename, opportunity.closedate from account.opportunities) from account";
    }
}
