package com.agun.flyJenkins.ui;

import java.util.List;
import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.AgentService;
import com.agun.flyJenkins.network.NetworkSpace;

import hudson.Extension;

@Extension
public class NetworkInfo extends FlyUI {

	@Override
	public String getDescription() {
		return "You can see network group of service";
	}

	public List<AgentService> getAgentServiceList(){
		NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
		List<AgentService> agentServiceList = networkSpace.getAgentServiceList();
		return agentServiceList;
	}
	
	 @Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
