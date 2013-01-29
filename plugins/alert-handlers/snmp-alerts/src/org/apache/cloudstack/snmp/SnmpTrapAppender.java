package org.apache.cloudstack.snmp;


import org.apache.cloudstack.alert.snmp.SnmpService;
import org.apache.cloudstack.api.PlugService;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

public class SnmpTrapAppender extends AppenderSkeleton {
    private static final Logger s_logger = Logger.getLogger(SnmpTrapAppender.class.getName());

    @PlugService
    private SnmpService _snmpService;

    @Override
    protected void append(LoggingEvent event) {
        s_logger.info(" in SNMP Appender");
        SnmpEnhancedPatternLayout l = null;
        if(getLayout() instanceof SnmpEnhancedPatternLayout){
            l = (SnmpEnhancedPatternLayout)getLayout();
        }

        if (!isAsSevereAsThreshold(event.getLevel())) return;

        SnmpTrapInfo info = l.parseEvent(event);

        /*SnmpManagerImpl.sendTrap(info.getAlertType(), info.getDataCenterId(), info.getPodId(),
                info.getClusterId(), info.getMessage());*/

        s_logger.info(" in null snmp appender " + _snmpService);

        if(_snmpService != null){
            _snmpService.sendSnmpTrap(info.getAlertType(), info.getDataCenterId(), info.getPodId(),
                    info.getClusterId(), info.getMessage());
        } else {
            s_logger.error(" SNMP Manager not initialized");
        }

        if (null == getLayout()) {
            errorHandler.error(new StringBuffer().append("No layout set for the Appender named [")
                    .append(getName())
                    .append(']').toString(),
                    null,
                    ErrorCode.MISSING_LAYOUT);
            return;
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
