package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;

import hudson.Extension;
import hudson.RelativePath;
import hudson.util.FormValidation;

@Extension
public class ConfigServiceGroup extends FlyUI {
	
	
	@Override
	public String getDescription() {
		return "You can setting meta info of service group";
	}

	public List<ServiceGroup> getServiceGroupList(){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		return serviceGroupSaveable.getServiceGroupList();
	}
	
	
	public void doSave(final StaplerRequest request, final StaplerResponse response) { 
		String groupName = request.getParameter("groupName");
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupName(groupName);
		
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
		
		if(serviceGroupList == null){
			serviceGroupList = new ArrayList<ServiceGroup>();
			serviceGroup.setGroupId(1);	
			serviceGroupList.add(serviceGroup);
			serviceGroupSaveable.setServiceGroupList(serviceGroupList);
			
			try {
				serviceGroupSaveable.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			int maxGroupId = 0;
			for(ServiceGroup indexServiceGroup : serviceGroupList){
				if(maxGroupId < indexServiceGroup.getGroupId()){
					maxGroupId = indexServiceGroup.getGroupId();
				}
			}
			maxGroupId++;
			serviceGroup.setGroupId(maxGroupId);	
			serviceGroupList.add(serviceGroup);
			serviceGroupSaveable.setServiceGroupList(serviceGroupList);
			try {
				serviceGroupSaveable.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Extension
	public static class DescriptorImpl extends FlyUIDescriptor {
		public FormValidation doCheckGroupName(@QueryParameter String value,
                   @RelativePath("groupName") @QueryParameter String name) {
			   	return FormValidation.ok("Are you sure " + name + " is a capital of " + value + "?");
		   }
	}
}
