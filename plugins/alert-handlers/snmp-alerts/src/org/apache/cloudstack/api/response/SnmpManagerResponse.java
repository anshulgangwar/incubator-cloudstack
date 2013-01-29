package org.apache.cloudstack.api.response;


import org.apache.cloudstack.alert.snmp.SnmpManagers;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;

@EntityReference(value = SnmpManagers.class)
public class SnmpManagerResponse extends BaseResponse {
    @SerializedName(ApiConstants.ID) @Param(description="the ID of the SNMP Manager")
    private String id;

    @SerializedName(ApiConstants.NAME) @Param(description="the name of the SNMP Manager")
    private String name;

    @SerializedName(ApiConstants.IP_ADDRESS) @Param(description="IP Address of the SNMP Manager")
    private String ipAddress;

    @SerializedName(ApiConstants.PORT) @Param(description="port of the SNMP Manager")
    private int port;

    @SerializedName(ApiConstants.ENABLED) @Param(description="SNMP Manager is enabled/disabled")
    private boolean enabled;

    @SerializedName(ApiConstants.COMMUNITY) @Param(description="Community of the SNMP Manager")
    private String community;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
