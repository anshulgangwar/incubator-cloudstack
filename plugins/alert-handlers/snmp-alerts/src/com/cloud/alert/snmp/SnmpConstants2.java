package com.cloud.alert.snmp;

import org.snmp4j.smi.OID;

/**
 * @author Anshul Gangwar
 *
 *IMPORTANT      
 * 
 * These OIDs are based on MIB file. If there is any change in MIB file
 * then that should be reflected in this file also * 
 * <br/><br/>
 * suffix 2 due to conflict with SnmpConstants class of snmp4j
 */
public class SnmpConstants2 {

    public static final short GENERAL_ALERT = 1;

    public static final short USAGE_ALERT   = 2;

    public static final int ROOT_OID = 45000;

    public static final OID CLOUDSTACK =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID});
    public static final OID ALERTS =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1});
    public static final OID TRAPS =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 0});
    public static final OID OBJECTS =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1});
    public static final OID CONFORMANCE =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2});
    public static final OID GROUPS =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2, 1});
    public static final OID COMPLIANCES =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2, 2});
    public static final OID ALERTS_TRAP =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 0, 1});
    public static final OID ALERT_TYPE =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1, 1});
    public static final OID DATACENTER_ID =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1, 2});
    public static final OID POD_ID =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1, 3});
    public static final OID CLUSTER_ID =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1, 4});
    public static final OID MESSAGE =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 1, 5});
    public static final OID COMPLIANCE =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2, 2, 1});
    public static final OID ALERTS_OBJECT_GROUP =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2, 1, 1});
    public static final OID ALERTS_NOTIFICATIONS_GROUP =
            new OID(new int[]{1, 3, 6, 1, 4, 1, ROOT_OID, 1, 2, 1, 2});

}

