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

package org.apache.cloudstack.alert.snmp;

import com.cloud.alert.Alert;
import com.cloud.utils.Pair;
import com.cloud.utils.component.PluggableService;
import org.apache.cloudstack.api.command.admin.alert.snmp.ListSnmpManagersCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.RegisterSnmpManagerCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.UpdateSnmpManagerCmd;
import org.apache.cloudstack.api.response.SnmpManagerResponse;

import java.util.Date;
import java.util.List;

public interface SnmpService extends PluggableService {
    /**
     * Register SNMP Manager details
     *
     * @return SNMP Manager details
     */
    public SnmpManagers registerSnmpManager(RegisterSnmpManagerCmd cmd);

    /**
     * Deletes the SNMP Manager
     *
     * @param id
     * @return true if deletion is successful and false otherwise
     */
    public boolean deleteSnmpManager(Long id);

    /**
     * Updates the SNMP Manager details
     *
     * @param cmd
     * @return SNMP Manager details
     */
    public SnmpManagers updateSnmpManager(UpdateSnmpManagerCmd cmd);

    /**
     * returns the lists of SNMP Managers
     *
     * @param cmd
     * @return the lists of SNMP Managers
     */
    public Pair<List<? extends SnmpManagers>, Integer> listSnmpManagers(ListSnmpManagersCmd cmd);

    public boolean getLastSnmpAlerts(Long snmpManagerId, Long alertId, Integer n, Date created,
                                     Pair<List<? extends Alert>, Integer> alertsPair);

    /**
     * sends the SNMP Trap
     *
     * @param alertType
     * @param dataCenterId
     * @param podId
     * @param clusterId
     * @param message
     */
    public void sendSnmpTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message,
                             Date generationTime);
    /**
     * creates the SNMP Manager Response
     *
     * @param snmpManagers
     * @return
     */
    public SnmpManagerResponse createSnmpManagerResponse(SnmpManagers snmpManagers);
}