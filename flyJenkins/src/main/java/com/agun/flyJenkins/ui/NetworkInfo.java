package com.agun.flyJenkins.ui;

import java.util.List;

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
		return NetworkSpace.getSaveAgentList();
	}
	
	 @Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
