package com.cloud.alert.snmp;


import org.junit.Before;
import org.junit.Test;

import javax.naming.ConfigurationException;

import static org.mockito.Mockito.mock;

public class SnmpManagerImplTest {
    SnmpManagerImpl _snmpManager = new SnmpManagerImpl();
    SnmpHelper helper;

    @Before
    public void setUp() throws ConfigurationException {
      helper = mock(SnmpHelper.class);
    }

    @Test
    public void sendSnmpTrapTest(){
        // when(helper.sendSnmpTrap((short)1, 1L, (Long)1L, (Long)1L,"test sending")).thenReturn(true);
    }
}
