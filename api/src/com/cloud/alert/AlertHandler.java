package com.cloud.alert;


import com.cloud.utils.component.Adapter;

public interface AlertHandler extends Adapter {
    public void sendAlert(short alertType, long dataCenterId, Long podId, Long clusterId, String subject, String content);
}
