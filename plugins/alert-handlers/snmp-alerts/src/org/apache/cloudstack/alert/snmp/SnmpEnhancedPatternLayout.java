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

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;
import java.util.StringTokenizer;

public class SnmpEnhancedPatternLayout extends EnhancedPatternLayout {
    private String pairDelimeter = "//";
    private String keyValueDelimeter = "::";

    public String getKeyValueDelimeter() {
        return keyValueDelimeter;
    }

    public void setKeyValueDelimeter(String keyValueDelimeter) {
        this.keyValueDelimeter = keyValueDelimeter;
    }

    public String getPairDelimeter() {
        return pairDelimeter;
    }

    public void setPairDelimeter(String pairDelimeter) {
        this.pairDelimeter = pairDelimeter;
    }

    public SnmpTrapInfo parseEvent(LoggingEvent event) {
        SnmpTrapInfo snmpTrapInfo = null;

        String message = event.getRenderedMessage();
        if (message.contains("alertType") && message.contains("message")) {
            snmpTrapInfo = new SnmpTrapInfo();
            final StringTokenizer splitter = new StringTokenizer(message, pairDelimeter);
            while (splitter.hasMoreTokens()) {
                final String variable = splitter.nextToken();
                final StringTokenizer varSplitter = new StringTokenizer(variable, keyValueDelimeter);
                String token1 = null;
                String token2 = null;

                if (varSplitter.hasMoreTokens()) {
                    token1 = varSplitter.nextToken().trim();
                } else {
                    return snmpTrapInfo;
                }

                if (varSplitter.hasMoreTokens()) {
                    token2 = varSplitter.nextToken().trim();
                } else {
                    return snmpTrapInfo;
                }

                if (token1.equalsIgnoreCase("alertType") && !token2.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setAlertType(Short.parseShort(token2));
                } else if (token1.equalsIgnoreCase("dataCenterId") && !token2.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setDataCenterId(Long.parseLong(token2));
                } else if (token1.equalsIgnoreCase("podId") && !token2.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setPodId(Long.parseLong(token2));
                } else if (token1.equalsIgnoreCase("clusterId") && !token2.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setClusterId(Long.parseLong(token2));
                } else if (token1.equalsIgnoreCase("message") && !token2.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setMessage(token2);
                }
            }

            snmpTrapInfo.setGenerationTime(new Date(event.getTimeStamp()));
        }
        return snmpTrapInfo;
    }
}