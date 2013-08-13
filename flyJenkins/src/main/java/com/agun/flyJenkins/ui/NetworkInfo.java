package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.service.ServiceGroup;

import hudson.Extension;

@Extension
public class NetworkInfo extends FlyUI {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "You can see network group of service";
	}

	public List<AgentService> getAgentList(){
		return NetworkSpace.getInstance().getSaveAgentList();
	}
	
	public Collection<List<AgentService>> getAgentListFromNetworkMap(){
		Map<String, List<AgentService>> networkMap = NetworkSpace.getInstance().getNetworkMap();

		Map<Integer, List<AgentService>> hashMap = new Hashtable<Integer, List<AgentService>>();
		for(List<AgentService> agentList : networkMap.values()){
			for(AgentService agentService : agentList){
				ServiceGroup serviceGroup =  agentService.getServiceGroup();
				if(hashMap.containsKey(serviceGroup.getGroupId())){
					hashMap.get(serviceGroup.getGroupId()).add(agentService);
				}else{
					List<AgentService> saveAgentList = new ArrayList<AgentService>();
					saveAgentList.add(agentService);
					hashMap.put(serviceGroup.getGroupId(), saveAgentList);
				}
			}
		}
		return hashMap.values();
	}
	
	 @Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
