/**
 * Agent 와 Service context 의 네트워크 map 을 관리하는 class
 * NetworkSpace 는 Singleton Instance 로 제공 된다.
 */
package com.agun.flyJenkins.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.agun.flyJenkins.FlyStore;
import java.util.Collections;


public class NetworkSpace {
	/**
	 * singleton Instance
	 */
	private static NetworkSpace networkSpace = new NetworkSpace();
	
	private Map<String, List<AgentService>> networkMap = new Hashtable<String, List<AgentService>>();
	
	
	private NetworkSpace(){
		
	}
	
	public static NetworkSpace getInstance(){
		return networkSpace;
	}
	
	/**
	 *  네트워크 스페이스를 최기화 구성 한다. 
	 *  
	 */
	public void initNetworkSpace(){
		System.out.println("=====> network start");
		
		List<AgentService> agentList =  this.getSaveAgentList();
		
		for(AgentService agentService : agentList){
			if(networkMap.containsKey(agentService.getHost())){
				List<AgentService> agentListFromMap = networkMap.get(agentService.getHost());
				agentListFromMap.add(agentService);
			}else{
				List<AgentService> agentListFromMap = new ArrayList<AgentService>();
				agentListFromMap.add(agentService);
				networkMap.put(agentService.getHost(), agentListFromMap);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<AgentService> getSaveAgentList(){
		ServerMeta serviceMeta = FlyStore.getServerMeta();
		
		if(serviceMeta == null)
			return Collections.emptyList();
		List<ServerMeta> serverMetaList =  serviceMeta.getServerMetaList();
		
		if(serverMetaList == null)
			return Collections.EMPTY_LIST;
		
		Map<Integer, ServiceGroup> serviceGroupMap = new Hashtable<Integer, ServiceGroup>();
		
		for(ServerMeta serverMetaData  : serverMetaList){
			if(serviceGroupMap.containsKey(serverMetaData.getGroupId())){
				ServiceGroup serviceGroup = serviceGroupMap.get(serverMetaData.getGroupId());
				serviceGroup.getServerMetaList().add(serverMetaData);
			}else{
				ServiceGroup serviceGroup = new ServiceGroup();
				serviceGroup.setGroupId(serverMetaData.getGroupId());
				List<ServerMeta> saveServerMetaList =  new ArrayList<ServerMeta>();
				saveServerMetaList.add(serverMetaData);
				serviceGroup.setServerMetaList(saveServerMetaList);
				serviceGroupMap.put(serverMetaData.getGroupId(), serviceGroup);
			}
		}
		List<AgentService> agentServiceList = new ArrayList<AgentService>();
		int agentId = 0 ;
		
		for(ServiceGroup serviceGroup : serviceGroupMap.values()){
			agentId++;
			AgentService agentService = new AgentService();
			agentService.setAgentId(agentId);
			agentService.setHost(serviceGroup.getServerMetaList().get(0).getHost());
			agentService.setServiceGroup(serviceGroup);
			agentServiceList.add(agentService);
		}
		return agentServiceList;
		
	}
	
	
	/**
	 * network map 에 지정된 Agent 를 추가 한다.
	 * @param AgentService
	 */
	public void attachAgent(AgentService agentService){
		if(agentService.getAgentId() < 1 
				|| agentService.getHost() == null 
				|| agentService.getHost().length() == 0)
			return; 
		if(networkMap.containsKey(agentService.getHost())){
			List<AgentService> agentServiceList = networkMap.get(agentService.getHost());
			for(AgentService agent : agentServiceList){
				if(agent.getAgentId() == agentService.getAgentId()){
					agentServiceList.remove(agent);
					break;
				}
			}
			agentServiceList.add(agentService);
		}else{
			List<AgentService> agentServiceList = new ArrayList<AgentService>();
			agentServiceList.add(agentService);
			networkMap.put(agentService.getHost(), agentServiceList);
		}
	}
	
	public AgentService getAgent(int agentId){
		for(List<AgentService> agentList : networkMap.values()){
			for(AgentService agent : agentList){
				if(agent.getAgentId() == agentId){
					return agent;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param host
	 * @return List<AgentService>
	 */
	public List<AgentService> getAgentList(String host){
		if(networkMap.containsKey(host)){
			return networkMap.get(host);
		}
		return Collections.EMPTY_LIST;
	}
	
	public Map<String, List<AgentService>> getNetworkMap(){
		return this.networkMap;
	}
}
