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

import com.cloud.utils.component.Manager;

/**
 * IMPORTANT</br>
 * Don't confuse the two Managers</br>
 * class name SnmpManager is named on the basis of Cloudstack naming
 * convention for components while in methods SnmpManager refers
 * to external entities which will receive the SNMP traps
 */
public interface SnmpManager extends Manager {
    /**
     * Returns true if at least one SNMP Manager of type is enabled
     * <p/>
     * <p/>
     * alerts type in broader category means general alerts ,
     * usage alerts ...
     * different from alertType in above function
     *
     * @return true if at least one SNMP Manager of type is enabled
     */
    public boolean isEnabled();
}