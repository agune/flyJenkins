package com.agun.agent.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  agent 에서 메모리에 유지되어야 하는 model 을 관리하는 class
 *  
 * @author pdc222
 *
 */
public class AgentMemoryStore {

	private static AgentMemoryStore agentMemory = new AgentMemoryStore();
	private List<AgentMeta> agentMetaList;
	private FlyJenkinsInfo flyJenkinsInfo = null;
	
	public static AgentMemoryStore getInstance(){
		return agentMemory;
	}

	private AgentMemoryStore(){
		agentMetaList = new ArrayList<AgentMeta>();
	}
	
	public void addAgentMeta(AgentMeta agentMeta){
		int agentId = delAgentMeta(agentMeta.getServiceId());
		if(agentId > 0)
			agentMeta.setId(agentId);
		this.agentMetaList.add(agentMeta);
	}
	
	public List<AgentMeta> getAgentMetaList(){
		return this.agentMetaList;
	}

	public AgentMeta getAgentMeta(int agentId){
		for(AgentMeta agentMeta : agentMetaList){
			if(agentMeta.getId() == agentId)
				return agentMeta;
		}
		return null;
	}

	public int getAgentTotalSize(){
		return agentMetaList.size();
	}
	
	public int delAgentMeta(int serviceId){
		int agentId = 0;
		if(agentMetaList == null)
			return 0;
		for(AgentMeta agentMeta : agentMetaList){
			if(agentMeta.getServiceId() == serviceId){
				agentId = agentMeta.getId();
				agentMetaList.remove(agentMeta);
				break;
			}
		}
		return agentId;
	}
	
	
	public FlyJenkinsInfo getFlyJenkinsInfo() {
		return flyJenkinsInfo;
	}

	public void setFlyJenkinsInfo(FlyJenkinsInfo flyJenkinsInfo) {
		this.flyJenkinsInfo = flyJenkinsInfo;
	}
	
	
	
}
