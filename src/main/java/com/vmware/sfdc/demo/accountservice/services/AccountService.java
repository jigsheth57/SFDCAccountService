package com.vmware.sfdc.demo.accountservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.force.api.ApiSession;
import com.force.api.ForceApi;
import com.vmware.sfdc.demo.accountservice.configuration.ApiProperties;
import com.vmware.sfdc.demo.accountservice.model.Account;
import com.vmware.sfdc.demo.accountservice.model.AccountList;
import com.vmware.sfdc.demo.accountservice.webclients.AuthServiceClient;

import reactor.core.publisher.Mono;

@Component
public class AccountService {
    private final AuthServiceClient authServiceClient;
    private final ApiProperties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    ForceApi api;

    public AccountService(AuthServiceClient authServiceClient, ApiProperties properties) {
        this.authServiceClient = authServiceClient;
        this.properties = properties;
    }

    public Mono<AccountList> getContactsByAccounts() {
        LOGGER.debug("getContactsByAccounts: properties.getQuery_contactByAccounts() {}",
                properties.getQuery_contactByAccounts());
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            return new AccountList(
                    this.api.query(properties.getQuery_contactByAccounts(), Account.class).getRecords());
        });
    }

    public Mono<AccountList> getOpportunitesByAccounts() {
        LOGGER.debug("getOpportunitesByAccounts: properties.getQuery_opportunityByAccounts() {}",
                properties.getQuery_opportunityByAccounts());
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            return new AccountList(
                    this.api.query(properties.getQuery_opportunityByAccounts(), Account.class).getRecords());
        });
    }

    public Mono<Account> getAccountById(String id) {
        LOGGER.debug("getAccountById: {}", id);
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            return api.getSObject("account", id).as(Account.class);
        });
    }

    public Mono<Account> updateAccount(Account account) {
        LOGGER.debug("updateAccount: {}", account);
        String id = account.getId();
        account.setId(null);
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            api.updateSObject("account", id, account);
            return api.getSObject("account", id).as(Account.class);
        });
    }

    public Mono<String> deleteAccount(String id) {
        LOGGER.debug("deleteAccount: {}", id);
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            api.deleteSObject("account", id);
            return String.format("Account {} deleted!", id);
        });
    }

    public Mono<Account> createAccount(Account account) {
        LOGGER.debug("createAccount: {}", account);
        return authServiceClient.getApiSession().map(apiSession -> {
            setApiSession(apiSession);
            String id = api.createSObject("account", account);
            account.setId(id);
            return account;
        });
    }

    private void setApiSession(ApiSession apiSession) {
        LOGGER.debug("setApiSession: accessToken={}", apiSession.getAccessToken());
        this.api = new ForceApi(apiSession);
    }

}
