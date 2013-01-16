package com.cloud.api.commands;

import com.cloud.alert.snmp.SnmpManagers;
import com.cloud.alert.snmp.SnmpService;
import com.cloud.api.response.SnmpManagerResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.utils.Pair;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@APICommand(name="listSnmpManagers", description="Lists the SNMP Managers which can receive trap", responseObject=SnmpManagerResponse.class)
public class ListSnmpManagersCmd extends BaseListCmd {
   public static final Logger s_logger = Logger.getLogger(ListSnmpManagersCmd.class.getName());

   private static final String s_name = "listsnmpmanagersresponse";

   /////////////////////////////////////////////////////
   //////////////// API parameters /////////////////////
   /////////////////////////////////////////////////////

   @Parameter(name= ApiConstants.ID, type=CommandType.UUID,  entityType = SnmpManagerResponse.class, description="ID of SNMP Manager to update")
   private Long id;

   @Parameter(name= ApiConstants.NAME, type=CommandType.STRING, description="name of the SNMP Manager")
   private String name;

   @Parameter(name=ApiConstants.IP_ADDRESS, type=CommandType.STRING,  description="IP address of the SNMP Manager")
   private String ipAddress;

   @Parameter(name=ApiConstants.PORT, type=CommandType.INTEGER, description="port of SNMP Manager")
   private Integer port;

   @Parameter(name=ApiConstants.ENABLED, type=CommandType.BOOLEAN, description="Enabled/Disabled the SNMP Manager")
   private Boolean enabled;

   @Parameter(name = ApiConstants.COMMUNITY, type = CommandType.STRING, description = "snmp community string to be used to contact SNMP Manager")
   private String community;

   /////////////////////////////////////////////////////
   /////////////////// Accessors ///////////////////////
   /////////////////////////////////////////////////////

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public Integer getPort() {
      return port;
   }

   public Boolean isEnabled() {
      return enabled;
   }

   public String getCommunity() {
      return community;
   }

   /////////////////////////////////////////////////////
   /////////////// API Implementation///////////////////
   /////////////////////////////////////////////////////

   @PlugService
   SnmpService _snmpService;

   @Override
    public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
      Pair<List<? extends SnmpManagers>, Integer> result = _snmpService.listSnmpManagers(this);
      ListResponse<SnmpManagerResponse> response = new ListResponse<SnmpManagerResponse>();
      List<SnmpManagerResponse> snmpManagersResponses = new ArrayList<SnmpManagerResponse>();
      for (SnmpManagers snmpManagers : result.first()) {
         SnmpManagerResponse smResponse = _snmpService.createSnmpManagerResponse(snmpManagers);
         snmpManagersResponses.add(smResponse);
      }
      response.setResponses(snmpManagersResponses, result.second());
      response.setResponseName(getCommandName());
      this.setResponseObject(response);
    }

    @Override
    public String getCommandName() {
       return s_name;
    }
}
