package com.vmware.sfdc.demo.accountservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SFDC Opportunity Object representation
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
public class Opportunity {

    @JsonProperty(value = "Id")
    String id;

    @JsonProperty(value = "Name")
    String name;

    @JsonProperty(value = "Description")
    String description;

    @JsonProperty(value = "NextStep")
    String nextStep;

    @JsonProperty(value = "LeadSource")
    String leadSource;

    @JsonProperty(value = "Type")
    String type;

    @JsonProperty(value = "Amount")
    Double amount;

    @JsonProperty(value = "StageName")
    String stagename;

    @JsonProperty(value = "CloseDate")
    Date closedate;

    @JsonProperty("AccountId")
    String accountId;

    @JsonProperty("Account")
    Account account;

}
