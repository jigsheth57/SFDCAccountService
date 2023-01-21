package com.vmware.sfdc.demo.accountservice.actuator;

public class SFDCStatus {

    private String status;

    public SFDCStatus() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SFDCStatus {" +
                "\"status\": \"" + status + "\"}";
    }

}
