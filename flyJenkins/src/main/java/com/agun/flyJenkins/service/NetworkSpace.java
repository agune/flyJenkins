/**
 * Agent 와 Service context 의 네트워크 map 을 관리하는 class
 */
package com.agun.flyJenkins.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class NetworkSpace {
	private Map<String, List<AgentService>> networkMap = new Hashtable<String, List<AgentService>>();
	
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
	
	public Map<String, List<AgentService>> getNetworkMap(){
		return this.networkMap;
	}
}
