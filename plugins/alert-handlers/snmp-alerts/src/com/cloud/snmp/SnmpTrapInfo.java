package com.cloud.snmp;


import java.util.Date;

public class SnmpTrapInfo {
    private String message;
    private long podId;
    private long dataCenterId;
    private long clusterId;
    private long accountId;
    private Date generationTime;
    private Short alertType;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Short getAlertType() {
        return alertType;
    }

    public void setAlertType(Short alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getPodId() {
        return podId;
    }

    public void setPodId(long podId) {
        this.podId = podId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public Date getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Date generationTime) {
        this.generationTime = generationTime;
    }
}
