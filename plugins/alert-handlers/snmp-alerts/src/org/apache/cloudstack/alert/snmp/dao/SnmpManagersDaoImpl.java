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

package org.apache.cloudstack.alert.snmp.dao;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchCriteria;
import org.apache.cloudstack.alert.snmp.SnmpManagersVO;

import javax.ejb.Local;
import java.util.List;

@Local(value = {SnmpManagersDao.class})
public class SnmpManagersDaoImpl extends GenericDaoBase<SnmpManagersVO, Long> implements SnmpManagersDao {

    @Override
    public List<SnmpManagersVO> getEnabledSnmpManagers() {
        SearchCriteria<SnmpManagersVO> sc = createSearchCriteria();
        sc.addAnd("enabled", SearchCriteria.Op.EQ, Boolean.valueOf(true));

        List<SnmpManagersVO> snmpManagers = listBy(sc);

        if (!snmpManagers.isEmpty()) {
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
        if (!snmpManagers.isEmpty()) {
            return snmpManagers;
        }

        return null;
    }
}