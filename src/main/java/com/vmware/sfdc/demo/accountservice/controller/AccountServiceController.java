package com.vmware.sfdc.demo.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.sfdc.demo.accountservice.model.Account;
import com.vmware.sfdc.demo.accountservice.model.AccountList;
import com.vmware.sfdc.demo.accountservice.services.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api")
public class AccountServiceController {
    private final AccountService accountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceController.class);

    public AccountServiceController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Returns result of account list based on query {$sfdc.query.contactByAccounts}
     * against SalesForce.com
     * 
     * @return AccountList
     */
    @GetMapping("/accounts")
    @Operation(summary = "Returns result of account list based on query {$sfdc.query.contactByAccounts} against SalesForce.com", description = "Returns result of account list based on query {$sfdc.query.contactByAccounts} against SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountList.class)))
    })
    public Mono<AccountList> getContactsByAccounts() {
        LOGGER.debug("getContactsByAccounts()");
        return accountService.getContactsByAccounts();
    }

    /**
     * Returns result of account list based on query
     * {$sfdc.query.opportunityByAccounts} against
     * SalesForce.com
     * 
     * @return AccountList
     */
    @GetMapping("/opp_by_accts")
    @Operation(summary = "Returns result of account list based on query {$sfdc.query.opportunityByAccounts} against SalesForce.com", description = "Returns result of account list based on query {$sfdc.query.opportunityByAccounts} against SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountList.class)))
    })
    public Mono<AccountList> getOpportunitesByAccounts() {
        LOGGER.debug("getOpportunitesByAccounts()");
        return accountService.getOpportunitesByAccounts();
    }

    /**
     * Returns result of an account by Id from SalesForce.com
     * 
     * @return Account
     */
    @GetMapping("/account/{accountId}")
    @Operation(summary = "Returns result of an account by Id from SalesForce.com", description = "Returns result of an account by Id from SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)))
    })
    public Mono<Account> getAccountById(@PathVariable String accountId) {
        LOGGER.debug("getAccountById() {}", accountId);
        return accountService.getAccountById(accountId);
    }

    /**
     * Update an account in SalesForce.com
     * 
     * @return Account
     */
    @PutMapping("/account")
    @Operation(summary = "Update an account in SalesForce.com", description = "Update an account in SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)))
    })
    public Mono<Account> updateAccount(@RequestBody Account account) {
        LOGGER.debug("updateAccount() {}", account);
        return accountService.updateAccount(account);
    }

    /**
     * Delete an account in SalesForce.com
     * 
     * @return String
     */
    @DeleteMapping("/account/{accountId}")
    @Operation(summary = "Delete an account in SalesForce.com", description = "Delete an account in SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    public Mono<String> deleteAccount(@PathVariable String accountId) {
        LOGGER.debug("deleteAccount() {}", accountId);
        return accountService.deleteAccount(accountId);
    }

    /**
     * Create an account in SalesForce.com
     * 
     * @return Account
     */
    @PostMapping("/account")
    @Operation(summary = "Create an account in SalesForce.com", description = "Create an account in SalesForce.com", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)))
    })
    public Mono<Account> createAccount(@RequestBody Account account) {
        LOGGER.debug("createAccount() {}", account);
        return accountService.createAccount(account);
    }

}
