package org.apache.cloudstack.alert.snmp;

import org.apache.cloudstack.api.Identity;
import org.apache.cloudstack.api.InternalIdentity;

/**
 * @author Anshul Gangwar
 *
 */
public interface SnmpManagers extends Identity, InternalIdentity {
    String getName();
    String getIpAddress();
    int getPort();
    String getCommunity();
    boolean isEnabled();
}
