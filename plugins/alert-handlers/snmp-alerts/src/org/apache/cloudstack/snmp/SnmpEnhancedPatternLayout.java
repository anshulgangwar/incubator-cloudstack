package org.apache.cloudstack.snmp;

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;
import java.util.StringTokenizer;


public class SnmpEnhancedPatternLayout extends EnhancedPatternLayout{
    private String pairDelimeter = "/";
    private String keyValueDelimeter = ":";

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

    public SnmpTrapInfo parseEvent(LoggingEvent event){
        SnmpTrapInfo snmpTrapInfo = new SnmpTrapInfo();

        String message = event.getRenderedMessage();
        final StringTokenizer splitter = new StringTokenizer(message, pairDelimeter);
        while (splitter.hasMoreTokens()) {
            final String variable = splitter.nextToken();
            final StringTokenizer varSplitter = new StringTokenizer(variable, keyValueDelimeter);
            String token1 = varSplitter.nextToken().trim();
            String token2 = varSplitter.nextToken().trim();

            if(token1.equalsIgnoreCase("alertType")  && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setAlertType(Short.parseShort(token2));
            } else if(token1.equalsIgnoreCase("accountId") && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setAccountId(Long.parseLong(token2));
            } else if(token1.equalsIgnoreCase("dataCenterId") && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setDataCenterId(Long.parseLong(token2));
            } else if(token1.equalsIgnoreCase("podId") && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setPodId(Long.parseLong(token2));
            } else if(token1.equalsIgnoreCase("clusterId") && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setClusterId(Long.parseLong(token2));
            } else if(token1.equalsIgnoreCase("message") && !token2.equalsIgnoreCase("null")){
                snmpTrapInfo.setMessage(token2);
            }
        }

        snmpTrapInfo.setGenerationTime(new Date(event.getTimeStamp()));

        return snmpTrapInfo;
    }


}
