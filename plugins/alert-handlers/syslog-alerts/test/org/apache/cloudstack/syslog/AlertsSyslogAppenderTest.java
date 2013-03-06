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

package org.apache.cloudstack.syslog;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.net.SyslogAppender;
import org.junit.Before;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AlertsSyslogAppenderTest {
    AlertsSyslogAppender _appender = new AlertsSyslogAppender();
    ArrayList<SyslogAppender> list = mock(ArrayList.class);

    @Before
    public void setUp() throws ConfigurationException {
        _appender.setLayout(new PatternLayout("%-5p [%c{3}] (%t:%x) %m%n"));
        _appender.setFacility("LOCAL6");
    }

    @Test
    public void setSyslogAppendersTest() {
        _appender.setSyslogHosts("10.1.1.1,10.1.1.2");
        assertEquals(" error Syslog Appenders list size not as expected ", 2, _appender._syslogAppenders.size());
    }

    @Test
    public void setSyslogAppendersNegativeTest() {
        _appender.setSyslogHosts("10.1.1.");
        assertTrue(" list was expected to be empty", _appender._syslogAppenders.isEmpty());
    }

    @Test
    public void appendTest() {
        String message = "alertType:: 14 // dataCenterId:: 0 // podId:: 0 // clusterId:: null // message:: Management" +
            " server node 127.0.0.1 is up";
        _appender.parseMessage(message);
        assertEquals(" message is not as expected ", "WARN   localhost   alertType:: managementNode   message:: Management " +
            "server node 127.0.0.1 is up", _appender._message.toString());
    }
}
