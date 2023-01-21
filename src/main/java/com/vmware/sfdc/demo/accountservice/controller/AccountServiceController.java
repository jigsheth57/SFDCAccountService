package com.vmware.sfdc.demo.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class AccountServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceController.class);
}
