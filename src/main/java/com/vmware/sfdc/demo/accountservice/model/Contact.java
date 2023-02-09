package com.vmware.sfdc.demo.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SFDC Contact Object representation
 * 
 * @author Jignesh Sheth
 *
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Department")
    private String department;

    @JsonProperty("Salutation")
    private String salutation;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("MobilePhone")
    private String mobilePhone;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("Account")
    private Account account;

    public Contact(String email, String firstName, String lastName) {
        this();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
