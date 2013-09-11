package com.agun.flyJenkins.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.agun.flyJenkins.model.AgentService;
import com.agun.flyJenkins.model.InstanceModel;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;


/**
 * this class constitution space of flyJenkins
 * @author agun
 *
 */

public class NetworkSpace {
	/**
	 * singleton Instance
	 */
	private static NetworkSpace networkSpace = new NetworkSpace();
	
	private Map<String, List<AgentService>> networkMap = new ConcurrentHashMap<String, List<AgentService>>();
	
	private Map<String, List<InstanceModel>> instanceModelMap = new ConcurrentHashMap<String, List<InstanceModel>>();
	
	private NetworkSpace(){ }
	
	public static NetworkSpace getInstance(){
		return networkSpace;
	}
	/**
	 * initialization network space
	 */
	public void initNetwork(){
		List<ServiceMeta> serviceMetaList = ServiceGroupSaveableUtil.getServiceMetaList();
		processInitNetwork(serviceMetaList);
	}
	
	
	/**
	 * attach service meta into network space
	 * @param serviceMeta
	 */
	public void attachServiceMeta(ServiceMeta serviceMeta){
		List<ServiceMeta> serviceMetaList = new ArrayList<ServiceMeta>();
		serviceMetaList.add(serviceMeta);
		processInitNetwork(serviceMetaList);
	}
	
	
	public ServiceMeta getServiceMeta(String host, int serviceId){
		List<AgentService> agentServiceList = this.getAgentListByHost(host);
		for(AgentService agentService : agentServiceList){
			List<ServiceMeta> serviceMetaList = agentService.getServiceMetaList();
			for(ServiceMeta serviceMeta : serviceMetaList){
				if(serviceId == serviceMeta.getServiceId())
					return serviceMeta;
			}
		}
		return null;
	
	}
	
	/**
	 * obtain agent of service list 
	 * @param host
	 * @return List<AgentService>
	 */
	public List<AgentService> getAgentListByHost(String host){
		if(networkMap.containsKey(host)){
			return networkMap.get(host);
		}
		return Collections.EMPTY_LIST;
	}
	
	public Map<String, List<AgentService>> getNetworkMap(){
		return networkMap;
	}
	
	public List<AgentService> getAgentServiceList(){
		if(networkMap.size() == 0)
			return Collections.EMPTY_LIST;
		
		List<AgentService> agentServiceList = new ArrayList<AgentService>(); 
		for(List<AgentService> partAgentServiceList : networkMap.values()){
			agentServiceList.addAll(partAgentServiceList);
		}
		return agentServiceList;
	}
	
	public Map<String, List<InstanceModel>> getInstanceModelMap(){
		return instanceModelMap;
	}
	
	private void processInitNetwork(List<ServiceMeta> serviceMetaList){
		
		int agentId = networkMap.size() + 1 ;
		
		for(ServiceMeta serviceMeta : serviceMetaList){
			
			// exist agent in networkMap
			if(networkMap.containsKey(serviceMeta.getHost())){
				List<AgentService> agentServiceList = networkMap.get(serviceMeta.getHost());
				for(AgentService agentService : agentServiceList){
					List<ServiceMeta> agentServiceMetaList = agentService.getServiceMetaList();
					agentServiceMetaList.add(serviceMeta);
					break;
				}
			// don't exist agent in networkMap
			}else{
				// create AgentService List
				List<AgentService> agentServiceList = new ArrayList<AgentService>();
				
				// create meta of service List
				List<ServiceMeta> agentServiceMetaList = new ArrayList<ServiceMeta>();
				// add meta of service into ServiceMeta List
				agentServiceMetaList.add(serviceMeta);

				// create agent service
				AgentService agentService = new AgentService();
				// set id into agent service		
				agentService.setAgentId(agentId);
				// set host into agent service
				agentService.setHost(serviceMeta.getHost());
				// set meta of service into agent service
				agentService.setServiceMetaList(agentServiceMetaList);
				
				//add agent service into agent service list
				agentServiceList.add(agentService);
				
				// add agent service list into network map
				networkMap.put(serviceMeta.getHost(), agentServiceList);
				
				agentId++;
			}
		}
	}

}
