package com.vmware.sfdc.demo.accountservice.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SFDC Account Object List representation
 * 
 * @author Jignesh Sheth
 *
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class AccountList {

    private List<Account> accounts;

    public AccountList(List<Account> accounts) {
        this.accounts = accounts;
    }
}
