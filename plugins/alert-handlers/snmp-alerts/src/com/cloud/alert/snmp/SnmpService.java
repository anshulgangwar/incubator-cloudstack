package com.cloud.alert.snmp;

import com.cloud.alert.Alert;
import com.cloud.api.commands.ListSnmpManagersCmd;
import com.cloud.api.commands.RegisterSnmpManagerCmd;
import com.cloud.api.commands.UpdateSnmpManagerCmd;
import com.cloud.api.response.SnmpManagerResponse;
import com.cloud.utils.Pair;
import com.cloud.utils.component.PluggableService;

import java.util.Date;
import java.util.List;

public interface SnmpService extends PluggableService {
    /**
     * Register SNMP Manager details
     * 
     * @return SNMP Manager details
     */
    public SnmpManagers registerSnmpManager(RegisterSnmpManagerCmd cmd);

    /**
     * Deletes the SNMP Manager
     * 
     * @return true if deletion is successful and false otherwise
     * @param id
     */
    public boolean deleteSnmpManager(Long id);

    /**
     * Updates the SNMP Manager details
     * 
     * @return SNMP Manager details
     * @param cmd
     */
    public SnmpManagers updateSnmpManager(UpdateSnmpManagerCmd cmd);

    /**
     * returns the lists of SNMP Managers
     * 
     * @return the lists of SNMP Managers
     * @param cmd
     */
    public Pair<List<? extends SnmpManagers>, Integer> listSnmpManagers(ListSnmpManagersCmd cmd);

   public boolean getLastSnmpAlerts(Long snmpManagerId, Long alertId, Integer n, Date created, Pair<List<? extends Alert>,Integer> alertsPair);

   /**
    * sends the SNMP Trap
    *
    * @param alertType
    * @param dataCenterId
    * @param podId
    * @param clusterId
    * @param message
    * alerts type in broader category means general alerts ,
    *            usage alerts ...
    *            different from alertType
    */
   public void sendSnmpTrap(short alertType, long dataCenterId, Long podId, Long clusterId, String message);

    public SnmpManagerResponse createSnmpManagerResponse(SnmpManagers snmpManagers);


}
