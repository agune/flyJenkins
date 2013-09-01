package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;






import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.AgentService;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;

import hudson.Extension;

@Extension
public class NetworkInfo extends FlyUI {

	@Override
	public String getDescription() {
		return "You can see network group of service";
	}

	public List<ServiceGroup> getServiceGroupList(){
		return ServiceGroupSaveableUtil.getServiceGroupList();
	}
	
	 @Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
