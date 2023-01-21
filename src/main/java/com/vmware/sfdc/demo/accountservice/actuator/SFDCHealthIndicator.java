package com.vmware.sfdc.demo.accountservice.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.vmware.sfdc.demo.accountservice.SFDCConstant;

@Component
public class SFDCHealthIndicator extends AbstractHealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFDCHealthIndicator.class);

    @Autowired
    SFDCStatusClient webClient;

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        SFDCStatus sfdcStatus = webClient.getStatus().block();
        LOGGER.debug(String.format("%s: %s", SFDCConstant.SFDC_INSTANCE.toLowerCase(), sfdcStatus.getStatus()));
        if (sfdcStatus.getStatus().equalsIgnoreCase("ok")) {
            builder.up()
                    .withDetail("instance", SFDCConstant.SFDC_INSTANCE.toLowerCase())
                    .withDetail("status", sfdcStatus.getStatus())
                    .withDetail("url", SFDCConstant.STATUS_URL);
        } else {
            builder.down()
                    .withDetail("instance", SFDCConstant.SFDC_INSTANCE.toLowerCase())
                    .withDetail("status", sfdcStatus.getStatus())
                    .withDetail("url", SFDCConstant.STATUS_URL);
        }
    }
}
