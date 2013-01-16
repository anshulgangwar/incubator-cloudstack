package com.cloud.alert.snmp.dao;

import com.cloud.alert.snmp.SnmpManagersVO;
import com.cloud.utils.db.GenericDao;

import java.util.List;

/**
 * @author Anshul Gangwar
 *
 */
public interface SnmpManagersDao extends GenericDao<SnmpManagersVO, Long> {
     /**
     * returns SNMP Managers which are enabled
     * @return  SNMP Managers which are enabled
     */
    List<SnmpManagersVO> getEnabledSnmpManagers();

   List<SnmpManagersVO> findbyIpAddressAndPort(String ipAddress, int port);
}
