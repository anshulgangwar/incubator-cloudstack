// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License

package org.apache.cloudstack.api.command.admin.alert.snmp;

import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;
import com.cloud.user.UserContext;
import org.apache.cloudstack.alert.snmp.SnmpManagers;
import org.apache.cloudstack.alert.snmp.SnmpService;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.SnmpManagerResponse;
import org.apache.log4j.Logger;

@APICommand(name = "registerSnmpManager", description = "Registers the SNMP Manager which can receive trap",
    responseObject = SnmpManagerResponse.class)
public class RegisterSnmpManagerCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(RegisterSnmpManagerCmd.class.getName());

    private static final String s_name = "registersnmpmanagerresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name = ApiConstants.NAME, type = CommandType.STRING, required = true,
        description = "name of the SNMP Manager")
    private String name;

    @Parameter(name = ApiConstants.IP_ADDRESS, type = CommandType.STRING, required = true,
        description = "IP address of the SNMP Manager")
    private String ipAddress;

    @Parameter(name = ApiConstants.PORT, type = CommandType.INTEGER, description = "If port is specified traps will " +
        "be sent to this port, default is 162.")
    private Integer port;

    @Parameter(name = ApiConstants.ENABLED, type = CommandType.BOOLEAN, description = "Enabled/Disabled the SNMP " +
        "Manager")
    private Boolean enabled;

    @Parameter(name = ApiConstants.COMMUNITY, type = CommandType.STRING, description = "snmp community string to be " +
        "used to contact SNMP Manager")
    private String community;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

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
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException,
        ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
        UserContext.current().setEventDetails("Registering SNMP Manager " + getIpAddress() + "/" + getPort());
        SnmpManagers snmpManagers = _snmpService.registerSnmpManager(this);
        if (snmpManagers != null) {
            SnmpManagerResponse response = _snmpService.createSnmpManagerResponse(snmpManagers);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to register SNMP Manager");
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