package com.cloud.api.commands;

import com.cloud.alert.snmp.SnmpManagers;
import com.cloud.alert.snmp.SnmpService;
import com.cloud.api.response.SnmpManagerResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;
import com.cloud.user.UserContext;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.log4j.Logger;

@APICommand(name="updateSnmpManager", description="Updates the SNMP Manager which can receive trap", responseObject=SnmpManagerResponse.class)
public class UpdateSnmpManagerCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(UpdateSnmpManagerCmd.class.getName());

    private static final String s_name = "updatesnmpmanagerresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name= ApiConstants.ID, type=CommandType.UUID,  entityType = SnmpManagerResponse.class, required=true, description="ID of SNMP Manager to update")
    private Long id;

    @Parameter(name= ApiConstants.NAME, type=CommandType.STRING, description="name of the SNMP Manager")
    private String name;

    @Parameter(name=ApiConstants.IP_ADDRESS, type=CommandType.STRING, description="IP address of the SNMP Manager")
    private String ipAddress;

    @Parameter(name=ApiConstants.PORT, type=CommandType.INTEGER, description="port of SNMP Manager")
    private Integer port;

    @Parameter(name=ApiConstants.ENABLED, type=CommandType.BOOLEAN, description="Enabled/Disabled the SNMP Manager")
    private Boolean enabled;

    @Parameter(name = ApiConstants.COMMUNITY, type = CommandType.STRING, description = "snmp community string to be used to contact SNMP Manager")
    private String community;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public String getCommunity() {
        return community;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @PlugService
    SnmpService _snmpService;

    @Override
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
        UserContext.current().setEventDetails("Registering SNMP Manager "+getIpAddress()+"/"+getPort());
        SnmpManagers snmpManagers = _snmpService.updateSnmpManager(this);
        if(snmpManagers != null){
            SnmpManagerResponse response = _snmpService.createSnmpManagerResponse(snmpManagers);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to update SNMP Manager");
        }
    }

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }

}
