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

import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SnmpTrapAppenderTest {
    SnmpTrapAppender _appender = new SnmpTrapAppender();
    LoggingEvent _event = mock(LoggingEvent.class);
    SnmpEnhancedPatternLayout _snmpEnhancedPatternLayout = mock(SnmpEnhancedPatternLayout.class);
    @Mock
    List<SnmpHelper> snmpHelpers;

    @Test
    public void appendTest(){
        _appender.setSnmpManagerIpAddresses("10.1.1.1,10.1.1.2");
        _appender.setSnmpManagerPorts("162,164");
        _appender.setSnmpManagerCommunities("public,snmp");

        _appender.setSnmpHelpers();
        assertEquals(" error snmpHelper list size not as expected ", _appender._snmpHelpers.size(), 2);

        /*appender.setSnmpManagerIpAddresses("10.1.1.1,10.1.1.2");
        appender.setSnmpManagerPorts("162,164");
        appender.setSnmpManagerCommunities("public,snmp");

        List<SnmpHelper> snmpHelpers
        System.out.print(" ye to aaye 0 ");
        SnmpTrapInfo info = new SnmpTrapInfo((short) 1, 1L, 1L, 1L, "test appender", new Date());

        when(l.parseEvent(any(LoggingEvent.class))).thenReturn(info);
        System.out.print(" ye to aaye 1");

        appender.setLayout(l);


        doNothing().when(appender._snmpHelpers).sendSnmpTrap((short) 1, 1L, 1L, 1L, "test appender", any(Date.class));



        System.out.print(" ye to aaye 3");

        appender.append(event);
        //
        assertEquals(" error snmpHelper list size not as expected ", appender._snmpHelpers.size(), 2);*/
    }

    @Test
    public void appendInvalidInputTest(){
        _appender.setSnmpManagerIpAddresses("10.1.1,10.1.1.2");
        _appender.setSnmpManagerPorts("162,164");
        _appender.setSnmpManagerCommunities("public,snmp");

        _appender.setSnmpHelpers();
        assertTrue(" list was expected to be empty", _appender._snmpHelpers.isEmpty());
    }
}
