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
// under the License.
package org.apache.cloudstack.api.command.user.securitygroup;

import org.apache.cloudstack.api.*;
import org.apache.log4j.Logger;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.response.SecurityGroupRuleResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import com.cloud.async.AsyncJob;
import com.cloud.event.EventTypes;
import com.cloud.network.security.SecurityGroup;
import com.cloud.user.Account;

@APICommand(name = "revokeSecurityGroupEgress", responseObject = SuccessResponse.class, description = "Deletes a particular egress rule from this security group", since="3.0.0")
public class RevokeSecurityGroupEgressCmd extends BaseAsyncCmd {
    public static final Logger s_logger = Logger.getLogger(RevokeSecurityGroupEgressCmd.class.getName());

    private static final String s_name = "revokesecuritygroupegress";

    // ///////////////////////////////////////////////////
    // ////////////// API parameters /////////////////////
    // ///////////////////////////////////////////////////

    @Parameter(name = ApiConstants.ID, type = CommandType.UUID, required = true, description = "The ID of the egress rule", entityType=SecurityGroupRuleResponse.class)
    private Long id;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    // ///////////////////////////////////////////////////
    // ///////////// API Implementation///////////////////
    // ///////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    public static String getResultObjectName() {
        return "revokesecuritygroupegress";
    }

    @Override
    public long getEntityOwnerId() {
        SecurityGroup group = _entityMgr.findById(SecurityGroup.class, getId());
        if (group != null) {
            return group.getAccountId();
        }

        return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_SECURITY_GROUP_REVOKE_EGRESS;
    }

    @Override
    public String getEventDescription() {
        return "revoking egress rule id: " + getId();
    }

    @Override
    public void execute() {
        boolean result = _securityGroupService.revokeSecurityGroupEgress(this);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to revoke security group egress rule");
        }
    }

    @Override
    public AsyncJob.Type getInstanceType() {
        return AsyncJob.Type.SecurityGroup;
    }

    @Override
    public Long getInstanceId() {
        return getId();
    }
}