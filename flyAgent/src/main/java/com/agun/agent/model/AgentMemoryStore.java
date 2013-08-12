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
	List<AgentMeta> agentMetaList;

	public static AgentMemoryStore getInstance(){
		return agentMemory;
	}

	private AgentMemoryStore(){
		agentMetaList = new ArrayList<AgentMeta>();
	}
	
	public void addAgentMeta(AgentMeta agentMeta){
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
	
}
