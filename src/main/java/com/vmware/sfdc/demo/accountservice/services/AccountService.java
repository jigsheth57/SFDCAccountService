package com.vmware.sfdc.demo.accountservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.force.api.ApiSession;
import com.force.api.ForceApi;
import com.vmware.sfdc.demo.accountservice.model.Account;
import com.vmware.sfdc.demo.accountservice.model.AccountList;
import com.vmware.sfdc.demo.accountservice.webclients.AuthServiceClient;

import reactor.core.publisher.Mono;

@Component
public class AccountService {
    private final AuthServiceClient authServiceClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    ForceApi api;

    private ReactiveValueOperations<String, Account> accountValueOps;
    private ReactiveValueOperations<String, AccountList> accountListValueOps;

    private String contactByAccounts;
    private String opportunityByAccounts;

    @Autowired
    public AccountService(AuthServiceClient authServiceClient,
            @Value("${sfdc.contactByAccounts}") String contactByAccounts,
            @Value("${sfdc.opportunityByAccounts}") String opportunityByAccounts,
            ReactiveRedisTemplate<String, Account> accountTemplate,
            ReactiveRedisTemplate<String, AccountList> accountListTemplate) {
        this.authServiceClient = authServiceClient;
        this.contactByAccounts = contactByAccounts;
        this.opportunityByAccounts = opportunityByAccounts;
        this.accountValueOps = accountTemplate.opsForValue();
        this.accountListValueOps = accountListTemplate.opsForValue();
    }

    public Mono<AccountList> getContactsByAccounts(HttpHeaders headers) {
        LOGGER.debug("getContactsByAccounts: contactByAccounts {}", contactByAccounts);
        return (Mono<AccountList>) retrieve("/accounts", headers);
    }

    public Mono<AccountList> getContactsByAccountsFallback(HttpHeaders headers) {
        LOGGER.debug("getContactsByAccountsFallback: contactByAccounts {}", contactByAccounts);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            AccountList accountList = new AccountList(this.api.query(contactByAccounts, Account.class).getRecords());
            try {
                store("/accounts", accountList);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return accountList;
        });
    }

    public Mono<AccountList> getOpportunitesByAccounts(HttpHeaders headers) {
        LOGGER.debug("getOpportunitesByAccounts: opportunityByAccounts {}", opportunityByAccounts);
        return (Mono<AccountList>) retrieve("/opp_by_accts", headers);
    }

    public Mono<AccountList> getOpportunitesByAccountsFallback(HttpHeaders headers) {
        LOGGER.debug("getOpportunitesByAccountsFallback: opportunityByAccounts {}", opportunityByAccounts);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            AccountList accountList = new AccountList(
                    this.api.query(opportunityByAccounts, Account.class).getRecords());
            try {
                store("/opp_by_accts", accountList);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return accountList;
        });
    }

    public Mono<Account> getAccountById(String id, HttpHeaders headers) {
        LOGGER.debug("getAccountById({})", id);
        return (Mono<Account>) retrieve(id, headers);
    }

    public Mono<Account> getAccountByIdFallback(String id, HttpHeaders headers) {
        LOGGER.debug("getAccountByIdFallback({})", id);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            Account account = api.getSObject("account", id).as(Account.class);
            try {
                store(account.getId(), account);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return account;
        });
    }

    public Mono<Account> updateAccount(Account account, HttpHeaders headers) {
        LOGGER.debug("updateAccount({})", account);
        String id = account.getId();
        account.setId(null);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            api.updateSObject("account", id, account);
            Account mod_account = api.getSObject("account", id).as(Account.class);
            try {
                store(mod_account.getId(), mod_account);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return mod_account;
        });
    }

    public Mono<String> deleteAccount(String id, HttpHeaders headers) {
        LOGGER.debug("deleteAccount({})", id);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            api.deleteSObject("account", id);
            try {
                remove(id);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return String.format("Account %s deleted!", id);
        });
    }

    public Mono<Account> createAccount(Account account, HttpHeaders headers) {
        LOGGER.debug("createAccount({})", account);
        return authServiceClient.getApiSession(headers).map(apiSession -> {
            setApiSession(apiSession);
            String id = api.createSObject("account", account);
            account.setId(id);
            try {
                store(account.getId(), account);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return account;
        });
    }

    private void setApiSession(ApiSession apiSession) {
        LOGGER.debug("setApiSession(accessToken={})", apiSession.getAccessToken());
        this.api = new ForceApi(apiSession);
    }

    private void store(String key, Object object) {
        LOGGER.debug("store({},{})", key, object);
        if (key != null) {
            if (object instanceof Account) {
                // accountValueOps = accountTemplate.opsForValue();
                accountValueOps.set(key, (Account) object).subscribe(result -> {
                    LOGGER.debug("Account({}) cached? {}", key, result);
                });
            } else if (object instanceof AccountList) {
                // accountListValueOps = accountListTemplate.opsForValue();
                accountListValueOps.set(key, (AccountList) object).subscribe(result -> {
                    LOGGER.debug("AccountList({}) cached? {}", key, result);
                });
            }
        }
    }

    private void remove(String key) {
        LOGGER.debug("remove({})", key);
        if (key != null) {
            if (!key.startsWith("/", 0)) {
                // accountValueOps = accountTemplate.opsForValue();
                accountValueOps.delete(key).subscribe(result -> {
                    LOGGER.debug("Account({}) removed from cached? {}", key, result);
                });
            }
        }
    }

    private Object retrieve(String key, HttpHeaders headers) {
        LOGGER.debug("retrieve({})", key);
        if (key != null) {
            if (!key.startsWith("/", 0)) {
                return accountValueOps.get(key)
                        .switchIfEmpty(Mono.defer(() -> getAccountByIdFallback(key, 
                                headers)))
                        .onErrorResume(err -> {
                            LOGGER.error(err.getMessage());
                            return Mono.empty();
                        });
            } else if (key.equals("/accounts")) {
                return accountListValueOps.get(key)
                        .switchIfEmpty(Mono.defer(() -> getContactsByAccountsFallback(
                                headers)))
                        .onErrorResume(err -> {
                            LOGGER.error(err.getMessage());
                            return Mono.empty();
                        });
            } else if (key.equals("/opp_by_accts")) {
                return accountListValueOps.get(key)
                        .switchIfEmpty(Mono.defer(() -> getOpportunitesByAccountsFallback(
                                headers)))
                        .onErrorResume(err -> {
                            LOGGER.error(err.getMessage());
                            return Mono.empty();
                        });
            }
        }
        return Mono.empty();
    }

}
