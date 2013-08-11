package com.agun.flyJenkins.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;

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
		System.out.println("====> network map" + networkMap.size());
		
		return networkMap.values();
	}
	
	 @Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
