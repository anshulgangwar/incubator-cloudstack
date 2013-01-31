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

package org.apache.cloudstack.snmp;

import org.apache.cloudstack.alert.snmp.SnmpServiceImpl;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

public class SnmpTrapAppender extends AppenderSkeleton {
    private static final Logger s_logger = Logger.getLogger(SnmpTrapAppender.class.getName());

    @Override
    protected void append(LoggingEvent event) {
        SnmpEnhancedPatternLayout l = null;

        if (null == getLayout()) {
            errorHandler.error(new StringBuffer().append("No layout set for the Appender named [")
                .append(getName())
                .append(']').toString(),
                null,
                ErrorCode.MISSING_LAYOUT);
            return;
        }

        if (getLayout() instanceof SnmpEnhancedPatternLayout) {
            l = (SnmpEnhancedPatternLayout) getLayout();
        } else {
            return;
        }

        if (!isAsSevereAsThreshold(event.getLevel())) {
            return;
        }

        SnmpTrapInfo info = l.parseEvent(event);

        if (info != null) {
            SnmpServiceImpl.sendTrap(info.getAlertType(), info.getDataCenterId(), info.getPodId(),
                info.getClusterId(), info.getMessage(), info.getGenerationTime());
        }
    }

    @Override
    public void close() {
        if (!closed) closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}