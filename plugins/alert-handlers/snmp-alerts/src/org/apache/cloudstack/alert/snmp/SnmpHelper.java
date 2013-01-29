package org.apache.cloudstack.alert.snmp;

import com.cloud.utils.exception.CloudRuntimeException;
import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * @author Anshul Gangwar
 * 
 */
public class
        SnmpHelper {
    private static final Logger s_logger = Logger.getLogger(SnmpHelper.class.getName());

    private Snmp snmp;
    private CommunityTarget target;
    private String address;

    public SnmpHelper(String address, String community) {
        this.address = address;
        setCommunityTarget(address, community);
        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
        } catch (IOException e) {
            snmp = null;
            throw new CloudRuntimeException(" Some error occured in crearting snmp object ");
        }

    }

    public void sendSnmpTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message) {
        s_logger.info(" sending SNMP trap to SNMP Managers");
        try {
            if (snmp != null) {
                snmp.send(createPDU(alertType, dataCenterId, podId, clusterId, message), target, null, null);
            }
        } catch (IOException e) {
            throw new CloudRuntimeException(" Some error occured in sending SNMP Trap");
        }
    }

    private PDU createPDU(short alertType, long dataCenterId, Long podId, Long clusterId, String message) {
        PDU trap = new PDU();
        trap.setType(PDU.TRAP);
       if(clusterId == null){
          clusterId = 0L;
       }

        trap.add(new VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants2.ALERTS_TRAP));
        trap.add(new VariableBinding(SnmpConstants2.ALERT_TYPE, new Integer32(alertType)));
        trap.add(new VariableBinding(SnmpConstants2.DATACENTER_ID, new UnsignedInteger32(dataCenterId)));
        trap.add(new VariableBinding(SnmpConstants2.POD_ID, new UnsignedInteger32(podId)));
        trap.add(new VariableBinding(SnmpConstants2.CLUSTER_ID, new UnsignedInteger32(clusterId)));
        trap.add(new VariableBinding(SnmpConstants2.MESSAGE, new OctetString(message)));


        return trap;
    }

    public CommunityTarget setCommunityTarget(String address, String community) {
        Address targetaddress = new UdpAddress(address);
        target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(targetaddress);
        return target;
    }

    public static void main(String[] args) {
        SnmpHelper s = new SnmpHelper("127.0.0.1/162", "public");
        for (short i = 0; i < 3; i++) {
            s.sendSnmpTrap(i, 20L, 30L, 30L,  " chalo kaam kar raha hai ");

        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
