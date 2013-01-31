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
import com.cloud.utils.Pair;
import org.apache.cloudstack.alert.snmp.SnmpManagers;
import org.apache.cloudstack.alert.snmp.SnmpService;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.SnmpManagerResponse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@APICommand(name = "listSnmpManagers", description = "Lists the SNMP Managers which can receive trap",
    responseObject = SnmpManagerResponse.class)
public class ListSnmpManagersCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListSnmpManagersCmd.class.getName());

    private static final String s_name = "listsnmpmanagersresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name = ApiConstants.ID, type = CommandType.UUID, entityType = SnmpManagerResponse.class,
        description = "ID of SNMP Manager to update")
    private Long id;

    @Parameter(name = ApiConstants.NAME, type = CommandType.STRING, description = "name of the SNMP Manager")
    private String name;

    @Parameter(name = ApiConstants.IP_ADDRESS, type = CommandType.STRING, description = "IP address of the SNMP " +
        "Manager")
    private String ipAddress;

    @Parameter(name = ApiConstants.PORT, type = CommandType.INTEGER, description = "port of SNMP Manager")
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
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException,
        ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
        Pair<List<? extends SnmpManagers>, Integer> result = _snmpService.listSnmpManagers(this);
        ListResponse<SnmpManagerResponse> response = new ListResponse<SnmpManagerResponse>();
        List<SnmpManagerResponse> snmpManagersResponses = new ArrayList<SnmpManagerResponse>();
        for (SnmpManagers snmpManagers : result.first()) {
            SnmpManagerResponse smResponse = _snmpService.createSnmpManagerResponse(snmpManagers);
            snmpManagersResponses.add(smResponse);
        }
        response.setResponses(snmpManagersResponses, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }

    @Override
    public String getCommandName() {
        return s_name;
    }
}