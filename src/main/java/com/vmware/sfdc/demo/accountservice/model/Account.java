package com.vmware.sfdc.demo.accountservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SFDC Account Object representation
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
public class Account {

    @JsonProperty(value = "Id")
    String id;

    @JsonProperty(value = "Name")
    String name;

    @JsonProperty(value = "Type")
    String type;

    @JsonProperty(value = "Description")
    String description;

    @JsonProperty(value = "Industry")
    String industry;

    @JsonProperty(value = "Ownership")
    String ownership;

    @JsonProperty(value = "Website")
    String website;

    @JsonProperty(value = "Phone")
    String phone;

    @JsonProperty(value = "NumberOfEmployees")
    Long numberOfEmployees;

    @JsonProperty("Opportunities")
    List<Opportunity> opportunities;

    @JsonProperty("Contacts")
    List<Contact> contacts;

}
