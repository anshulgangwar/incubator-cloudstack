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

import com.cloud.utils.net.NetUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AlertsSyslogAppender extends AppenderSkeleton {
    String _syslogHosts = null;
    String _delimeter = ",";
    List<String> _syslogHostsList = null;
    List<SyslogAppender> _syslogAppenders = null;
    private String _facility;
    StringBuilder _message = null;
    private String _pairDelimeter = "//";
    private String _keyValueDelimeter = "::";

    @Override
    protected void append(LoggingEvent event) {
        if (!isAsSevereAsThreshold(event.getLevel())) {
            return;
        }

        try {
            parseMessage(event.getRenderedMessage());
            if (_message != null) {
                LoggingEvent syslogEvent = new LoggingEvent(event.getFQNOfLoggerClass(), event.getLogger(),
                    event.getLevel(), _message.toString(), null);
                if (_syslogAppenders != null && !_syslogAppenders.isEmpty()) {
                    for (SyslogAppender syslogAppender : _syslogAppenders) {
                        syslogAppender.append(syslogEvent);
                    }
                }
            }
        } catch (Exception e) {
            errorHandler.error(e.getMessage());
        }
    }

    @Override
    synchronized public void close() {
        for (SyslogAppender syslogAppender : _syslogAppenders) {
            syslogAppender.close();
        }
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    void setSyslogAppenders() {
        if (_syslogAppenders == null) {
            _syslogAppenders = new ArrayList<SyslogAppender>();
        }

        if (_syslogHosts == null || _syslogHosts.trim().isEmpty()) {
            reset();
            return;
        }

        _syslogHostsList = parse(_syslogHosts);

        if (!validateIpAddresses()) {
            reset();
            errorHandler.error(" Invalid format for the IP Addresses parameter ");
            return;
        }

        for (String syslogHost : _syslogHostsList) {
            _syslogAppenders.add(new SyslogAppender(getLayout(), syslogHost, SyslogAppender.getFacility(_facility)));
        }
    }

    public static String getAlertTypeString(short alertType) {
        switch (alertType) {
            case 0:
                return "availableMemory";
            case 1:
                return "availableCpu";
            case 2:
                return "availableStorage";
            case 3:
                return "remainingStorageAllocated";
            case 4:
                return "unallocatedVirtualNetworkpublicIp";
            case 5:
                return "unallocatedPrivateIp";
            case 6:
                return "availableSecondaryStorage";
            case 7:
                return "host";
            case 8:
                return "userVmState";
            case 9:
                return "domainRouterVmState ";
            case 10:
                return "consoleProxyVmState";
            case 11:
                return "routingConnection";
            case 12:
                return "storageIssueSystemVms";
            case 13:
                return "usageServerStatus";
            case 14:
                return "managementNode";
            case 15:
                return "domainRouterMigrate";
            case 16:
                return "consoleProxyMigrate";
            case 17:
                return "userVmMigrate";
            case 18:
                return "unallocatedVlan";
            case 19:
                return "ssvmStopped";
            case 20:
                return "usageServerResult";
            case 21:
                return "storageDelete";
            case 22:
                return "updateResourceCount";
            case 23:
                return "usageSanityResult";
            case 24:
                return "unallocatedDirectAttachedPublicIp";
            case 25:
                return "unallocatedLocalStorage";
            case 26:
                return "resourceLimitExceeded";
            default:
                return "unknown";
        }
    }

    private List<String> parse(String str) {
        List<String> result = new ArrayList<String>();

        final StringTokenizer tokenizer = new StringTokenizer(str, _delimeter);
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken().trim());
        }
        return result;
    }

    private boolean validateIpAddresses() {
        for (String ipAddress : _syslogHostsList) {
            if (ipAddress.trim().equalsIgnoreCase("localhost")) {
                continue;
            }
            if (!NetUtils.isValidIp(ipAddress)) {
                return false;
            }
        }
        return true;
    }

    public void parseMessage(String logMessage) {
        _message = null;
        if (logMessage.contains("alertType") && logMessage.contains("message")) {
            short alertType = -1;
            long dataCenterId = 0;
            long podId = 0;
            long clusterId = 0;
            String sysMessage = null;

            final StringTokenizer messageSplitter = new StringTokenizer(logMessage, _pairDelimeter);
            while (messageSplitter.hasMoreTokens()) {
                final String pairToken = messageSplitter.nextToken();
                final StringTokenizer pairSplitter = new StringTokenizer(pairToken, _keyValueDelimeter);
                String keyToken = null;
                String valueToken = null;

                if (pairSplitter.hasMoreTokens()) {
                    keyToken = pairSplitter.nextToken().trim();
                } else {
                    break;
                }

                if (pairSplitter.hasMoreTokens()) {
                    valueToken = pairSplitter.nextToken().trim();
                } else {
                    break;
                }

                if (keyToken.equalsIgnoreCase("alertType") && !valueToken.equalsIgnoreCase("null")) {
                    alertType = Short.parseShort(valueToken);
                } else if (keyToken.equalsIgnoreCase("dataCenterId") && !valueToken.equalsIgnoreCase("null")) {
                    dataCenterId = Long.parseLong(valueToken);
                } else if (keyToken.equalsIgnoreCase("podId") && !valueToken.equalsIgnoreCase("null")) {
                    podId = Long.parseLong(valueToken);
                } else if (keyToken.equalsIgnoreCase("clusterId") && !valueToken.equalsIgnoreCase("null")) {
                    clusterId = Long.parseLong(valueToken);
                } else if (keyToken.equalsIgnoreCase("message") && !valueToken.equalsIgnoreCase("null")) {
                    sysMessage = getSyslogMessage(logMessage);
                }
            }

            _message = new StringBuilder();
            _message.append(severityOfAlert(alertType) + "   ");
            InetAddress ip;
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                ip = null;
            }

            if (ip != null) {
                _message.append(ip.getHostName() + "   ");
            } else {
                _message.append("unknown" + "   ");
            }

            if (alertType > 0) {
                _message.append("alertType" + _keyValueDelimeter + " " + getAlertTypeString(alertType) + "   ");
                if (dataCenterId != 0) {
                    _message.append("dataCenterId" + _keyValueDelimeter + " " + dataCenterId +
                        "   ");
                }

                if (podId != 0) {
                    _message.append("podId" + _keyValueDelimeter + " " + podId + "   ");
                }

                if (clusterId != 0) {
                    _message.append("clusterId" + _keyValueDelimeter + " " + clusterId + "   ");
                }

                if (sysMessage != null) {
                    _message.append("message" + _keyValueDelimeter + " " + sysMessage);
                } else {
                    errorHandler.error(" What is the use of alert without message ");
                }
            } else {
                errorHandler.error(" Invalid alert Type ");
            }
        }
    }

    private String getSyslogMessage(String message) {
        int lastIndexOfKeyValueDelimeter = message.lastIndexOf(_keyValueDelimeter);
        int lastIndexOfMessageInString = message.lastIndexOf("message");

        if (lastIndexOfKeyValueDelimeter - lastIndexOfMessageInString <= 9) {
            return message.substring(lastIndexOfKeyValueDelimeter + _keyValueDelimeter.length()).trim();
        } else if (lastIndexOfMessageInString < lastIndexOfKeyValueDelimeter) {
            return message.substring(lastIndexOfMessageInString + _keyValueDelimeter.length() + 8).trim();
        }

        return message.substring(message.lastIndexOf("message" + _keyValueDelimeter) + 9).trim();
    }

    private void reset() {
        _syslogAppenders.clear();
    }

    public void setFacility(String facility) {
        if (facility == null) {
            return;
        }

        this._facility = facility;
        if (_syslogAppenders != null && !_syslogAppenders.isEmpty()) {
            for (SyslogAppender syslogAppender : _syslogAppenders) {
                syslogAppender.setFacility(facility);
            }
        }
    }

    private String severityOfAlert(short alertType) {
        switch (alertType) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 19:
            case 20:
                return "CRITICAL";
            default:
                return "WARN";
        }
    }

    public String getFacility() {
        return _facility;
    }

    public String getSyslogHosts() {
        return _syslogHosts;
    }

    public void setSyslogHosts(String syslogHosts) {
        this._syslogHosts = syslogHosts;
        this.setSyslogAppenders();
    }

    public String getDelimeter() {
        return _delimeter;
    }

    public void setDelimeter(String delimeter) {
        this._delimeter = delimeter;
    }

    public String getPairDelimeter() {
        return _pairDelimeter;
    }

    public void setPairDelimeter(String pairDelimeter) {
        this._pairDelimeter = pairDelimeter;
    }

    public String getKeyValueDelimeter() {
        return _keyValueDelimeter;
    }

    public void setKeyValueDelimeter(String keyValueDelimeter) {
        this._keyValueDelimeter = keyValueDelimeter;
    }
}