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
package org.apache.cloudstack.alert.snmp.dao;

import org.apache.cloudstack.alert.snmp.SnmpManagersVO;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchCriteria;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Anshul Gangwar
 *
 */
@Local(value = { SnmpManagersDao.class })
public class SnmpManagersDaoImpl extends GenericDaoBase<SnmpManagersVO, Long> implements SnmpManagersDao {

    @Override
    public List<SnmpManagersVO> getEnabledSnmpManagers() {
        SearchCriteria<SnmpManagersVO> sc = createSearchCriteria();
        sc.addAnd("enabled", SearchCriteria.Op.EQ, Boolean.valueOf(true));

        List<SnmpManagersVO> snmpManagers = listBy(sc);

        if(!snmpManagers.isEmpty()){
            return snmpManagers;
        }

        return null;
    }

   @Override
   public List<SnmpManagersVO> findbyIpAddressAndPort(String ipAddress, int port) {
      SearchCriteria<SnmpManagersVO> sc = createSearchCriteria();
      sc.addAnd("ipAddress", SearchCriteria.Op.EQ, ipAddress);
      sc.addAnd("port", SearchCriteria.Op.EQ, Integer.valueOf(port));

      List<SnmpManagersVO> snmpManagers = listBy(sc);
      if(!snmpManagers.isEmpty()){
         return snmpManagers;
      }

      return null;
   }


}
