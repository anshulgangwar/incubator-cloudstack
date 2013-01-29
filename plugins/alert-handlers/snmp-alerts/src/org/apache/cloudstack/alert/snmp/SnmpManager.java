/**
 * Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.apache.cloudstack.alert.snmp;

import com.cloud.utils.component.Manager;

/**
 * IMPORTANT</br>
 * Don't confuse the two Managers</br>
 * class name SnmpManager is named on the basis of Cloudstack naming
 * convention for components while in methods SnmpManager refers
 * to external entities which will receive the SNMP traps
 * 
 * @author Anshul Gangwar
 * 
 * 
 */
public interface SnmpManager extends Manager {
     /**
     * Returns true if at least one SNMP Manager of type is enabled
     * 
     *
     *            alerts type in broader category means general alerts ,
     *            usage alerts ...
     *            different from alertType in above function
     * @return true if at least one SNMP Manager of type is enabled
     *
     */
    public boolean isEnabled();

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
}
