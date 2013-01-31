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
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.utils.Pair;
import com.cloud.utils.component.Inject;
import com.cloud.utils.db.Filter;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.utils.net.NetUtils;
import org.apache.cloudstack.alert.snmp.dao.SnmpManagersDao;
import org.apache.cloudstack.api.command.admin.alert.snmp.DeleteSnmpManagerCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.GetLastSnmpAlertsCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.ListSnmpManagersCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.RegisterSnmpManagerCmd;
import org.apache.cloudstack.api.command.admin.alert.snmp.UpdateSnmpManagerCmd;
import org.apache.cloudstack.api.response.SnmpManagerResponse;
import org.apache.log4j.Logger;

import javax.ejb.Local;
import javax.naming.ConfigurationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Local(value = {SnmpManager.class, SnmpService.class})
public class SnmpServiceImpl implements SnmpManager, SnmpService {
    private static final Logger s_logger = Logger.getLogger(SnmpServiceImpl.class.getName());

    private static List<SnmpHelper> s_snmpHelpers = null;

    private String _name = null;
    @Inject
    SnmpManagersDao _snmpManagersDao;

    @Override
    public boolean configure(String name, Map<String, Object> params) throws ConfigurationException {
        _name = name;
        initializeSnmpHelpers();
        return true;
    }

    private void initializeSnmpHelpers() {
        s_logger.info(" initializing SNMP Managers");
        s_snmpHelpers = new ArrayList<SnmpHelper>();

        List<SnmpManagersVO> sms = _snmpManagersDao.getEnabledSnmpManagers();

        if (sms != null) {
            String address;
            String community;

            for (SnmpManagersVO sm : sms) {
                address = sm.getIpAddress() + "/" + sm.getPort();
                community = sm.getCommunity();
                s_snmpHelpers.add(new SnmpHelper(address, community));
            }
        } else {
            s_logger.info("No SNMP Manager defined to initialize");
        }
        s_logger.info(" SNMP Managers initialized ");
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public SnmpManagers registerSnmpManager(RegisterSnmpManagerCmd cmd) {
        s_logger.info(" Registering SNMP Manager");
        //initialize to default values if not given
        int port = cmd.getPort() != null ? cmd.getPort() : 162;
        String community = cmd.getCommunity() != null ? cmd.getCommunity() : "public";
        boolean enabled = cmd.isEnabled() != null ? cmd.isEnabled() : true;

        validateIpAddressAndPort(cmd.getIpAddress(), port);

        if (_snmpManagersDao.findbyIpAddressAndPort(cmd.getIpAddress(), port) != null) {
            throw new CloudRuntimeException(" SNMP Manager with IP address: " + cmd.getIpAddress() + " and port: " +
                port + " already exists");
        }

        SnmpManagersVO snmpManagerVO = new SnmpManagersVO(cmd.getName(), cmd.getIpAddress(), port, community, enabled);
        SnmpManagersVO result = _snmpManagersDao.persist(snmpManagerVO);
        if (result != null) {
            initializeSnmpHelpers();
            s_logger.info(" SNMP Manager registered ");
        } else {
            throw new CloudRuntimeException(" Failed to register SNMP Manager with IP address: " + cmd.getIpAddress()
                + " and port: " + port);
        }
        return result;
    }

    private void validateIpAddressAndPort(String ipAddress, int port) {
        validateIpAddress(ipAddress);

        validatePort(port);
    }

    private void validatePort(int port) {
        if (!NetUtils.isValidPort(port)) {
            throw new InvalidParameterValueException(" Invalid port value port: " + port);
        }
    }

    private void validateIpAddress(String ipAddress) {
        if (!NetUtils.isValidIp(ipAddress)) {
            throw new InvalidParameterValueException("Invalid format for the IP Address parameter, " +
                "IP Address: " + ipAddress);
        }
    }

    @Override
    public boolean deleteSnmpManager(Long id) {
        s_logger.info(" Deleting SNMP Manager with id : " + id);
        SnmpManagersVO sm = _snmpManagersDao.findById(id);

        if (sm == null) {
            throw new CloudRuntimeException(" SNMP Manager with id: " + id + " does not exist ");
        }

        boolean result = _snmpManagersDao.remove(id);
        if (!result) {
            throw new CloudRuntimeException(" not able to delete SNMP Manager with id " + id);
        }

        initializeSnmpHelpers();

        s_logger.info(" SNMP Manager with id : " + id + " is deleted ");
        return result;
    }

    @Override
    public SnmpManagers updateSnmpManager(UpdateSnmpManagerCmd cmd) {
        Long snmpManagerId = cmd.getId();
        String name = cmd.getName();
        String ipAddress = cmd.getIpAddress();
        Integer port = cmd.getPort();
        Boolean enabled = cmd.isEnabled();
        String community = cmd.getCommunity();

        s_logger.info(" Updating SNMP Manager with id : " + snmpManagerId);

        SnmpManagersVO sm = _snmpManagersDao.findById(snmpManagerId);

        if (sm == null) {
            throw new CloudRuntimeException(" SNMP Manager with id: " + snmpManagerId + " does not exist ");
        }

        if (name != null) {
            sm.setName(name);
        }

        if (ipAddress != null) {
            validateIpAddress(ipAddress);
            sm.setIpAddress(ipAddress);
        }

        if (port != null) {
            validatePort(port);
            sm.setPort(port);
        }

        if (enabled != null) {
            sm.setEnabled(enabled);
        }

        if (community != null) {
            sm.setCommunity(community);
        }

        boolean success = _snmpManagersDao.update(snmpManagerId, sm);

        if (!success) {
            throw new CloudRuntimeException(" Failed to update SNMP Manager: " + snmpManagerId);
        }

        initializeSnmpHelpers();

        s_logger.info(" SNMP Manager with id : " + snmpManagerId + " is successfully updated ");

        return sm;
    }

    @Override
    public Pair<List<? extends SnmpManagers>, Integer> listSnmpManagers(ListSnmpManagersCmd cmd) {
        s_logger.info(" Listing SNMP Managers");
        Filter searchFilter = new Filter(SnmpManagersVO.class, "id", true, cmd.getStartIndex(), cmd.getPageSizeVal());
        SearchCriteria<SnmpManagersVO> sc = _snmpManagersDao.createSearchCriteria();

        if (cmd.getId() != null) {
            sc.addAnd("id", SearchCriteria.Op.EQ, cmd.getId());
        }

        if (cmd.getName() != null) {
            sc.addAnd("name", SearchCriteria.Op.EQ, cmd.getName());
        }

        if (cmd.getIpAddress() != null) {
            sc.addAnd("ipAddress", SearchCriteria.Op.EQ, cmd.getIpAddress());
        }

        if (cmd.getPort() != null) {
            sc.addAnd("port", SearchCriteria.Op.EQ, cmd.getPort());
        }

        if (cmd.isEnabled() != null) {
            sc.addAnd("enabled", SearchCriteria.Op.EQ, cmd.isEnabled());
        }

        if (cmd.getCommunity() != null) {
            sc.addAnd("community", SearchCriteria.Op.EQ, cmd.getCommunity());
        }

        Pair<List<SnmpManagersVO>, Integer> result = _snmpManagersDao.searchAndCount(sc, searchFilter);
        return new Pair<List<? extends SnmpManagers>, Integer>(result.first(), result.second());
    }

    @Override
    public boolean getLastSnmpAlerts(Long snmpManagerId, Long alertId, Integer n, Date created,
                                     Pair<List<? extends Alert>, Integer> alertsPair) {
        s_logger.info(" sending missed alerts ");
        SnmpManagersVO smvo = _snmpManagersDao.findById(snmpManagerId);
        SnmpHelper targetHelper = null;

        if (smvo == null) {
            throw new CloudRuntimeException(" SNMP Manager with id: " + snmpManagerId + " does not exist");
        }

        if (!smvo.isEnabled()) {
            throw new CloudRuntimeException(" SNMP Manager with id: " + snmpManagerId + " is not enabled");
        }

        String address;
        for (SnmpHelper h : s_snmpHelpers) {
            address = smvo.getIpAddress() + "/" + smvo.getPort();
            if (h.getAddress().trim().equalsIgnoreCase(address.trim())) {
                targetHelper = h;
                break;
            }
        }

        boolean send = false;
        int count = alertsPair.second();
        for (Alert alert : alertsPair.first()) {
            if (!send) {
                send = checkSendCondition(alertId, n, created, alert, count);
            }

            if (send) {
                //TODO change this null after change in Alert
                targetHelper.sendSnmpTrap(alert.getType(), alert.getDataCenterId(), alert.getPodId(),
                    alert.getClusterId(), alert.getSubject(), null);
            }
        }

        if (!send) {
            if (alertId != null) {
                throw new CloudRuntimeException(" this alert id is not yet generated ");
            }
            throw new CloudRuntimeException(" You have not missed any alerts, if that's not the case check your " +
                "parameters ");
        } else {
            s_logger.info(" sending missed alerts was successful ");
        }
        return true;
    }

    private boolean checkSendCondition(Long alertId, Integer n, Date created, Alert alert, int count) {
        if (n == null && created == null && alertId == null) {
            return true;
        }

        if (n != null && n >= count) {
            return true;
        }

        if (created != null && created.compareTo(alert.getCreatedDate()) <= 0) {
            return true;
        }

        if (alertId != null && alertId <= alert.getId()) {
            return true;
        }

        return false;
    }

    @Override
    public void sendSnmpTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message,
                             Date generationTime) {
        if (isEnabled()) {
            s_logger.info(" sending SNMP trap to SNMP Managers");
            for (SnmpHelper h : s_snmpHelpers) {
                h.sendSnmpTrap(alertType, dataCenterId, podId, clusterId, message, generationTime);
            }
        }
    }

    public static void sendTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message,
                                Date generationTime) {
        if (!s_snmpHelpers.isEmpty()) {
            s_logger.info(" sending SNMP trap to SNMP Managers ");
            for (SnmpHelper h : s_snmpHelpers) {
                h.sendSnmpTrap(alertType, dataCenterId, podId, clusterId, message, generationTime);
            }
        } else {
            s_logger.info(" No SNMP Mangers are there to send trap ");
        }
    }

    @Override
    public boolean isEnabled() {
        if (s_snmpHelpers.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public SnmpManagerResponse createSnmpManagerResponse(SnmpManagers snmpManagers) {
        SnmpManagerResponse response = new SnmpManagerResponse();
        response.setId(snmpManagers.getUuid());
        response.setIpAddress(snmpManagers.getIpAddress());
        response.setCommunity(snmpManagers.getCommunity());
        response.setEnabled(snmpManagers.isEnabled());
        response.setName(snmpManagers.getName());
        response.setPort(snmpManagers.getPort());
        response.setObjectName("snmpmanager");
        return response;
    }

    @Override
    public List<Class<?>> getCommands() {
        List<Class<?>> cmdList = new ArrayList<Class<?>>();
        cmdList.add(RegisterSnmpManagerCmd.class);
        cmdList.add(DeleteSnmpManagerCmd.class);
        cmdList.add(ListSnmpManagersCmd.class);
        cmdList.add(UpdateSnmpManagerCmd.class);
        cmdList.add(GetLastSnmpAlertsCmd.class);
        return cmdList;
    }
}