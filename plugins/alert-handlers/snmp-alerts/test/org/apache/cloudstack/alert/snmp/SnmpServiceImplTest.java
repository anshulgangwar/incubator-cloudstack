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

import org.apache.cloudstack.alert.snmp.dao.SnmpManagersDao;
import org.apache.cloudstack.api.command.admin.alert.snmp.RegisterSnmpManagerCmd;
import org.junit.Before;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SnmpServiceImplTest {
    SnmpServiceImpl _snmpService = new SnmpServiceImpl();
    SnmpHelper _helper = mock(SnmpHelper.class);
    SnmpManagersDao _snmpManagersDao = mock(SnmpManagersDao.class);

    @Before
    public void setUp() throws ConfigurationException {
        _snmpService._snmpManagersDao = _snmpManagersDao;
    }

    @Test
    public void sendSnmpTrapTest() {
        doNothing().when(_helper).sendSnmpTrap((short) 1, 1L, (Long) 1L, (Long) 1L, "test sending", new Date());
    }

    @Test
    public void registerSnmpManagerTest() {
        RegisterSnmpManagerCmd cmd = mock(RegisterSnmpManagerCmd.class);
        when(cmd.getName()).thenReturn("firstsnmpmanager");
        when(cmd.getIpAddress()).thenReturn("127.0.0.1");
        when(cmd.getPort()).thenReturn(162);
        when(cmd.getCommunity()).thenReturn("public");
        when(cmd.isEnabled()).thenReturn(true);

        SnmpManagersVO snmpManagersVO = new SnmpManagersVO("firstsnmpmanager", "127.0.0.1", 162, "public", true);

        when(_snmpManagersDao.findbyIpAddressAndPort(anyString(), anyInt())).thenReturn(null);

        List<SnmpManagersVO> snmpManagersVOList = new ArrayList<SnmpManagersVO>();
        snmpManagersVOList.add(snmpManagersVO);

        when(_snmpManagersDao.getEnabledSnmpManagers()).thenReturn(snmpManagersVOList);

        when(_snmpManagersDao.persist((SnmpManagersVO) anyObject())).thenReturn(snmpManagersVO);
        assertEquals(" error not equal ", _snmpService.registerSnmpManager(cmd), snmpManagersVO);
        assertTrue(_snmpService.isEnabled());
    }

    @Test
    public void deleteSnmpManagerTest() {
        SnmpManagersVO snmpManagersVO = new SnmpManagersVO("firstsnmpmanager", "127.0.0.1", 162, "public", true);
        when(_snmpManagersDao.findById(anyLong())).thenReturn(snmpManagersVO);
        when(_snmpManagersDao.remove(anyLong())).thenReturn(true);

        when(_snmpManagersDao.getEnabledSnmpManagers()).thenReturn(null);

        assertTrue("some error in deleting ", _snmpService.deleteSnmpManager(1L));
    }
}
