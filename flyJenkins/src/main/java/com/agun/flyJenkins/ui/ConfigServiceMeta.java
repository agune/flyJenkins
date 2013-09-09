package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.AgentService;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.network.NetworkSpace;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;
import com.agun.flyJenkins.user.FlyUser;

import hudson.Extension;

@Extension
public class ConfigServiceMeta extends FlyUI {

	public ConfigServiceMeta() {}
	
	
	/**
	 * obtain service group list
	 * @return List<ServiceGroup>
	 */
	public List<ServiceGroup> getServiceGroupList(){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		
		if(serviceGroupSaveable.getServiceGroupList() == null)
			return Collections.EMPTY_LIST;
		
		return serviceGroupSaveable.getServiceGroupList();
	}
	
	/**
	 * obtain agent of service map 
	 * @return
	 */
	public Map<String, List<AgentService>> getAgentServiceMap(){
		NetworkSpace  networkSpace = FlyFactory.getNetworkSpace();
		return networkSpace.getNetworkMap();
	}
	
	
	
    /**
     * save meta info of service 
     * @param request
     * @param response
     */
    public void doSave(final StaplerRequest request, final StaplerResponse response) { 

    	/**
    	 * 권한 체크
    	 */
    	if(FlyUser.isFlyRoot() == false)
    		return;
    	
    	String host  = request.getParameter("host");
    	String command  = request.getParameter("command");
        String destination =  request.getParameter("destination");
     	String testUrl  = request.getParameter("testUrl");
    	int groupId  = Integer.parseInt(request.getParameter("groupId"));
    	int type  = Integer.parseInt(request.getParameter("type"));
    	int weight = Integer.parseInt(request.getParameter("weight"));
   
    	ServiceMeta serviceMeta = new ServiceMeta();
    	serviceMeta.setHost(host);
        serviceMeta.setCommand(command);
        serviceMeta.setDestination(destination);
        serviceMeta.setGroupId(groupId);
    	serviceMeta.setTestUrl(testUrl);
    	serviceMeta.setType(type);
    	serviceMeta.setWeight(weight);
    	
    	ServiceGroupSaveableUtil.saveServiceMeta(serviceMeta);
       
    	/**
         * 네트워크 space 에 추가 한다.
         */
    	NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
    	networkSpace.attachServiceMeta(serviceMeta);
    	
        try {
			response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
  
   public boolean isFlyRoot(){
	  return  FlyUser.isFlyRoot();
   }
    
    @Override
    public String getDescription() {
        return "You can setting meta info of deploy service";
    }

      
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends FlyUIDescriptor {
    	
    
    }
}
