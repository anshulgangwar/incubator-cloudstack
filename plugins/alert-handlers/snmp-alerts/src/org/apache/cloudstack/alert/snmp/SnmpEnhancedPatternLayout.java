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
    private String _pairDelimeter = "//";
    private String _keyValueDelimeter = "::";

    public String getKeyValueDelimeter() {
        return _keyValueDelimeter;
    }

    public void setKeyValueDelimeter(String keyValueDelimeter) {
        this._keyValueDelimeter = keyValueDelimeter;
    }

    public String getPairDelimeter() {
        return _pairDelimeter;
    }

    public void setPairDelimeter(String pairDelimeter) {
        this._pairDelimeter = pairDelimeter;
    }

    public SnmpTrapInfo parseEvent(LoggingEvent event) {
        SnmpTrapInfo snmpTrapInfo = null;

        final String message = event.getRenderedMessage();
        if (message.contains("alertType") && message.contains("message")) {
            snmpTrapInfo = new SnmpTrapInfo();
            final StringTokenizer messageSplitter = new StringTokenizer(message, _pairDelimeter);
            while (messageSplitter.hasMoreTokens()) {
                final String pairToken = messageSplitter.nextToken();
                final StringTokenizer pairSplitter = new StringTokenizer(pairToken, _keyValueDelimeter);
                String keyToken = null;
                String valueToken = null;

                if (pairSplitter.hasMoreTokens()) {
                    keyToken = pairSplitter.nextToken().trim();
                } else {
                    return snmpTrapInfo;
                }

                if (pairSplitter.hasMoreTokens()) {
                    valueToken = pairSplitter.nextToken().trim();
                } else {
                    return snmpTrapInfo;
                }

                if (keyToken.equalsIgnoreCase("alertType") && !valueToken.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setAlertType(Short.parseShort(valueToken));
                } else if (keyToken.equalsIgnoreCase("dataCenterId") && !valueToken.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setDataCenterId(Long.parseLong(valueToken));
                } else if (keyToken.equalsIgnoreCase("podId") && !valueToken.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setPodId(Long.parseLong(valueToken));
                } else if (keyToken.equalsIgnoreCase("clusterId") && !valueToken.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setClusterId(Long.parseLong(valueToken));
                } else if (keyToken.equalsIgnoreCase("message") && !valueToken.equalsIgnoreCase("null")) {
                    snmpTrapInfo.setMessage(getSnmpMessage(message));
                }
            }

            snmpTrapInfo.setGenerationTime(new Date(event.getTimeStamp()));
        }
        return snmpTrapInfo;
    }

    private String getSnmpMessage(String message) {
        int lastIndexOfKeyValueDelimeter = message.lastIndexOf(_keyValueDelimeter);
        int lastIndexOfMessageInString = message.lastIndexOf("message");

        if (lastIndexOfKeyValueDelimeter - lastIndexOfMessageInString <= 9) {
            return message.substring(lastIndexOfKeyValueDelimeter + _keyValueDelimeter.length()).trim();
        } else if (lastIndexOfMessageInString < lastIndexOfKeyValueDelimeter) {
            return message.substring(lastIndexOfMessageInString + _keyValueDelimeter.length() + 8).trim();
        }

        return message.substring(message.lastIndexOf("message" + _keyValueDelimeter) + 9).trim();
    }
}