package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.network.NetworkSpace;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;
import com.agun.flyJenkins.request.RequestMap;
import com.agun.flyJenkins.request.RequestQueue;
import com.agun.flyJenkins.schedule.PeriodWork;

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
		
		int groupId = 0; 
		
		if(request.getParameter("groupId") != null)
			groupId = Integer.parseInt(request.getParameter("groupId"));
		
		
		/**
		 * case delete
		 */
		if(request.getParameter("mode") != null){
			if(request.getParameter("mode").equals("del")){
				
				
				PeriodWork periodWork = FlyFactory.getPeriodWork();
    			RequestQueue requestQueue = periodWork.getRequestQueue();
    	    
    			List<ServiceMeta> serviceMetaList = ServiceGroupSaveableUtil.getServiceMetaListByGroupId(groupId);
    			for(ServiceMeta serviceMeta : serviceMetaList){
	    			Map<String, Object> argMap = new Hashtable<String, Object>();
	    			argMap.put("serviceId", serviceMeta.getServiceId());
	    			RequestMap requestMap = new RequestMap();
	    			requestMap.setType(4);
	    			requestMap.setArg(argMap);
	    			requestQueue.add(serviceMeta.getHost(), requestMap);
    			}
				
				ServiceGroupSaveableUtil.delServiceGroup(groupId);
				NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
				networkSpace.reload();
				
				
				try {
					response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		
		String groupName = request.getParameter("groupName");
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
	
		
		/**
		 * case edit
		 */
		
		if(groupId > 0){
			boolean isEdit = false;
			for(ServiceGroup serviceGroup :  serviceGroupList){
				if(serviceGroup.getGroupId() == groupId){
					serviceGroup.setGroupName(groupName);
					isEdit= true;
					break;
				}
			}
			if(isEdit){
				serviceGroupSaveable.setServiceGroupList(serviceGroupList);
				try {
					serviceGroupSaveable.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		/**
		 * case insert
		 */
		
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupName(groupName);
		
		
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
		
		try {
			response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
		} catch (IOException e) {
			e.printStackTrace();
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
