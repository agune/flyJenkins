package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
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
import com.agun.flyJenkins.request.RequestMap;
import com.agun.flyJenkins.request.RequestQueue;
import com.agun.flyJenkins.schedule.PeriodWork;
import com.agun.flyJenkins.user.FlyUser;
import com.agun.flyJenkins.util.FlyJenkinsEnv;

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
		
		NetworkSpace  networkSpace = FlyFactory.getNetworkSpace();
		
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
		for(ServiceGroup serviceGroup : serviceGroupList){
			List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
			if(serviceMetaList != null){
				for(ServiceMeta serviceMeta : serviceMetaList){
					ServiceMeta networkServiceMeta = networkSpace.getServiceMeta(serviceMeta.getServiceId());
					serviceMeta.setPid(networkServiceMeta.getPid());
				}
			}
		}
		return serviceGroupList;
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
    	int serviceId = 0;
    	
    	if(request.getParameter("serviceId") != null){
    		serviceId = Integer.parseInt(request.getParameter("serviceId"));
    	}
    	
    	/**
    	 * case delete 
    	 */
    	if(request.getParameter("mode") != null){
    		if(request.getParameter("mode").equals("del")){
    			
    			ServiceMeta serviceMeta = new ServiceMeta();
    			serviceMeta.setServiceId(serviceId);
    			ServiceGroupSaveableUtil.delServiceMeta(serviceMeta);
    			PeriodWork periodWork = FlyFactory.getPeriodWork();
    			RequestQueue requestQueue = periodWork.getRequestQueue();
    	    
    			Map<String, Object> argMap = new Hashtable<String, Object>();
    			argMap.put("serviceId", serviceId);
    		
    			RequestMap requestMap = new RequestMap();
    			requestMap.setType(4);
    			requestMap.setArg(argMap);
    	
    			NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
    			ServiceMeta networkServiceMeta = networkSpace.getServiceMeta(serviceId);
    			
    			requestQueue.add(networkServiceMeta.getHost(), requestMap);
    			networkSpace.reload();
    	    
    	    	
    			try {
    				response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			return;
    		}
    	}
    	
    	ServiceMeta serviceMeta = new ServiceMeta();
    	serviceMeta.setHost(host);
        serviceMeta.setCommand(command);
        serviceMeta.setDestination(destination);
        serviceMeta.setGroupId(groupId);
    	serviceMeta.setTestUrl(testUrl);
    	serviceMeta.setType(type);
    	serviceMeta.setWeight(weight);
    	serviceMeta.setInstallAble(true);
    	serviceMeta.setServiceId(serviceId);
    	
    	int copyService =0;
    	if(request.getParameter("copyService") != null){
    	 	copyService  = Integer.parseInt(request.getParameter("copyService"));
    	}
    	
    	
    	if(copyService  > 0){
   		 	serviceMeta.setType(getCopyServiceType(copyService));
   		   	serviceMeta.setInstallAble(false);
   		   	serviceMeta.setDependenceService(copyService);
    	}
   
    	if(serviceId > 0){
    		ServiceGroupSaveableUtil.delServiceMeta(serviceMeta);
    	}
    	ServiceGroupSaveableUtil.saveServiceMeta(serviceMeta);
       
    	/**
         * 네트워크 space 에 추가 한다.
         */
    	NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
    	networkSpace.reload();
    	
    	
    	PeriodWork periodWork = FlyFactory.getPeriodWork();
		RequestQueue requestQueue = periodWork.getRequestQueue();
    	
		/**
		 * case copy service
		 */
    	if(copyService > 0){
	    	Map<String, Object> argMap = new Hashtable<String, Object>();
			argMap.put("host", host);
			argMap.put("production", FlyJenkinsEnv.getLastBuildPath(serviceMeta.getDependenceService()));
			argMap.put("serviceType", serviceMeta.getType());
			argMap.put("destination", serviceMeta.getDestination());
			
			RequestMap requestMap = new RequestMap();
			requestMap.setType(2);
			requestMap.setArg(argMap);
    	
			/**
			 * request 를 queue 저장
			 */
		
			requestQueue.add(host, requestMap);
    	}
		
    	/**
    	 * case attach service
    	 */
    	Map<String, Object> argMap = new Hashtable<String, Object>();
		argMap.put("host", host);
		argMap.put("serviceId", serviceMeta.getServiceId());
		argMap.put("type", serviceMeta.getType());
		argMap.put("command", serviceMeta.getCommand());
		argMap.put("testUrl", serviceMeta.getTestUrl());
		argMap.put("destination", serviceMeta.getDestination());
				
		RequestMap requestMap = new RequestMap();
		requestMap.setType(3);
		requestMap.setArg(argMap);
    	
		requestQueue.add(host, requestMap);
    	
        try {
			response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
		} catch (IOException e) {
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

    
    private int getCopyServiceType(int serviceId){
    	NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
    	ServiceMeta serviceMeta = networkSpace.getServiceMeta(serviceId);
    	if(serviceMeta == null)
    		return 0;
    	return serviceMeta.getType();
    }
      
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends FlyUIDescriptor {
    	
    
    }
}
