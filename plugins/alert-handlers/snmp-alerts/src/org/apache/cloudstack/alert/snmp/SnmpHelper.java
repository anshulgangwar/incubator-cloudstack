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

import com.cloud.utils.exception.CloudRuntimeException;
import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Date;

public class SnmpHelper {
    private static final Logger s_logger = Logger.getLogger(SnmpHelper.class.getName());

    private Snmp _snmp;
    private CommunityTarget _target;
    private String _address;

    public SnmpHelper(String address, String community) {
        this._address = address;
        setCommunityTarget(address, community);
        try {
            _snmp = new Snmp(new DefaultUdpTransportMapping());
        } catch (IOException e) {
            _snmp = null;
            throw new CloudRuntimeException(" Some error occured in crearting snmp object ");
        }
    }

    public void sendSnmpTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message, Date
        generationTime) {
        s_logger.info(" sending SNMP trap to SNMP Managers");
        try {
            if (_snmp != null) {
                _snmp.send(createPDU(alertType, dataCenterId, podId, clusterId, message, generationTime), _target,
                    null, null);
            }
        } catch (IOException e) {
            throw new CloudRuntimeException(" Some error occured in sending SNMP Trap");
        }
    }

    private PDU createPDU(short alertType, Long dataCenterId, Long podId, Long clusterId, String message, Date
        generationTime) {
        PDU trap = new PDU();
        trap.setType(PDU.TRAP);

        alertType++;
        if (alertType > 0) {
            trap.add(new VariableBinding(SnmpConstants.snmpTrapOID, getOID(SnmpConstants2.TRAPS_PREFIX + alertType)));
            if (dataCenterId != null) {
                trap.add(new VariableBinding(getOID(SnmpConstants2.DATA_CENTER_ID),
                    new UnsignedInteger32(dataCenterId)));
            } else {
                trap.add(new VariableBinding(getOID(SnmpConstants2.DATA_CENTER_ID)));
            }

            if (podId != null) {
                trap.add(new VariableBinding(getOID(SnmpConstants2.POD_ID), new UnsignedInteger32(podId)));
            } else {
                trap.add(new VariableBinding(getOID(SnmpConstants2.POD_ID)));
            }

            if (clusterId != null) {
                trap.add(new VariableBinding(getOID(SnmpConstants2.CLUSTER_ID), new UnsignedInteger32(clusterId)));
            } else {
                trap.add(new VariableBinding(getOID(SnmpConstants2.CLUSTER_ID)));
            }

            if (message != null) {
                trap.add(new VariableBinding(getOID(SnmpConstants2.MESSAGE), new OctetString(message)));
            } else {
                trap.add(new VariableBinding(getOID(SnmpConstants2.MESSAGE)));
            }

            if (generationTime != null) {
                trap.add(new VariableBinding(getOID(SnmpConstants2.GENERATION_TIME),
                    new OctetString(generationTime.toString())));
            } else {
                trap.add(new VariableBinding(getOID(SnmpConstants2.GENERATION_TIME)));
            }
        } else {
            throw new CloudRuntimeException(" Invalid alert Type");
        }

        return trap;
    }

    private OID getOID(String oidString) {
        return new OID(oidString);
    }

    public CommunityTarget setCommunityTarget(String address, String community) {
        Address targetaddress = new UdpAddress(address);
        _target = new CommunityTarget();
        _target.setCommunity(new OctetString(community));
        _target.setVersion(SnmpConstants.version2c);
        _target.setAddress(targetaddress);
        return _target;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        this._address = address;
    }
}