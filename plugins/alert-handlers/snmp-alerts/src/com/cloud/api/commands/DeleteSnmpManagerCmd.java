package com.cloud.api.commands;

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
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.log4j.Logger;


@APICommand(name="deleteSnmpManager", description="Deletes the SNMP Manager which can receive trap", responseObject=SuccessResponse.class)
public class DeleteSnmpManagerCmd  extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(DeleteSnmpManagerCmd.class.getName());
    private static final String s_name = "deletesnmpmanagerresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name= ApiConstants.ID, type=CommandType.UUID,  entityType = SnmpManagerResponse.class, required=true, description="ID of SNMP Manager to delete")
    private Long id;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @PlugService
    SnmpService _snmpService;


    @Override
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
        UserContext.current().setEventDetails("Snmp Manager Id: "+getId());
        boolean result = _snmpService.deleteSnmpManager(id);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to delete SNMP Manager");
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
