package com.cloud.alert.snmp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author Anshul Gangwar
 *
 */
@Entity
@Table(name = "snmp_managers")
public class SnmpManagersVO implements SnmpManagers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private int port;

    @Column(name = "community")
    private String community;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "uuid")
    private String uuid;

    public SnmpManagersVO() {
        this.uuid = UUID.randomUUID().toString();
    }

    public SnmpManagersVO(long id, String name, String ipAddress, int port, short type, String community, boolean enabled) {
        this(name, ipAddress, port, community, enabled);
        this.id = id;
    }

    public SnmpManagersVO(String name, String ipAddress, int port, String community, boolean enabled) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
        this.community = community;
        this.enabled = enabled;
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
