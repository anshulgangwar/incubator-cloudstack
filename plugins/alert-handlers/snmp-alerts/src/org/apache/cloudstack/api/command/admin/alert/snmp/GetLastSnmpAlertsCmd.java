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

import com.cloud.alert.Alert;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;
import com.cloud.utils.Pair;
import org.apache.cloudstack.alert.snmp.SnmpService;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.command.admin.resource.ListAlertsCmd;
import org.apache.cloudstack.api.response.AlertResponse;
import org.apache.cloudstack.api.response.SnmpManagerResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

@APICommand(name = "getLastSnmpAlerts", description = "sends the missed SNMP traps again based on n, created time, " +
    "alertid parameter values passed", responseObject = SuccessResponse.class)
public class GetLastSnmpAlertsCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(GetLastSnmpAlertsCmd.class.getName());

    private static final String s_name = "getlastsnmpalertsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name = ApiConstants.SNMP_MANAGER_ID, type = CommandType.UUID, entityType = SnmpManagerResponse.class,
        required = true, description = " SNMP Manager id")
    private Long snmpManagerId;

    @Parameter(name = ApiConstants.ALERT_ID, type = CommandType.UUID, entityType = AlertResponse.class,
        description = "alert id ")
    private Long alertId;

    @Parameter(name = ApiConstants.N, type = CommandType.INTEGER, description = " these number of last alerts will be" +
        " sent ")
    private Integer n;

    @Parameter(name = ApiConstants.CREATED, type = CommandType.DATE, description = " SNMP alerts will be sent for " +
        "after this creation time  ")
    private Date created;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @PlugService
    SnmpService _snmpService;

    @Override
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException,
        ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
        Pair<List<? extends Alert>, Integer> alerts = _mgr.searchForAlerts(new ListAlertsCmd());
        boolean result = _snmpService.getLastSnmpAlerts(snmpManagerId, alertId, n, created, alerts);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to send Alerts again");
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