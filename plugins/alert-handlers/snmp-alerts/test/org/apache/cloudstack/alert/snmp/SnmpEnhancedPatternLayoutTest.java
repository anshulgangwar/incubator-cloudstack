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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SnmpEnhancedPatternLayoutTest {
    LoggingEvent _event = mock(LoggingEvent.class);
    SnmpEnhancedPatternLayout _snmpEnhancedPatternLayout = new SnmpEnhancedPatternLayout();

    @Test
    public void parseAlertTest() {
        when(_event.getRenderedMessage()).thenReturn(" alertType:: 14 // dataCenterId:: 1 // podId:: 1 // " +
            "clusterId:: null // message:: Management network CIDR is not configured originally. Set it default to 10" +
            ".102.192.0/22");
        _snmpEnhancedPatternLayout.setKeyValueDelimeter("::");
        _snmpEnhancedPatternLayout.setPairDelimeter("//");

        SnmpTrapInfo info = _snmpEnhancedPatternLayout.parseEvent(_event);
        assertEquals(" alert type not as expected ", 14, info.getAlertType());
        assertEquals(" data center id not as expected ", 1, info.getDataCenterId());
        assertEquals(" pod id os not as expected ", 1, info.getPodId());
        assertEquals(" cluster id is not as expected ", 0, info.getClusterId());
        assertEquals(" message is not as expected ", "Management network CIDR is not configured originally. Set it " +
            "default to 10.102.192.0/22", info.getMessage());
    }

    @Test
    public void parseRandomTest(){
        when(_event.getRenderedMessage()).thenReturn("Problem clearing email alert");
        assertNull(" Null value was expected ", _snmpEnhancedPatternLayout.parseEvent(_event));
    }
}
