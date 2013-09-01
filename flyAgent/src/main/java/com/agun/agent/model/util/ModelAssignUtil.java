package com.agun.agent.model.util;

import java.util.Map;

import com.agun.agent.model.AgentMeta;

public class ModelAssignUtil {

	public static void assignAgentMeta(AgentMeta agentMeta, Map<String, Object> valueMap){
		
		if(valueMap.containsKey("id")){
			agentMeta.setId((Integer)valueMap.get("id"));
		}
		
		if(valueMap.containsKey("serviceId")){
			agentMeta.setServiceId((Integer)valueMap.get("serviceId"));
		}
	
		if(valueMap.containsKey("type")){
			agentMeta.setType((Integer)valueMap.get("type"));
		}
	
		if(valueMap.containsKey("pid")){
			agentMeta.setPid((Integer)valueMap.get("pid"));
		}
		
		if(valueMap.containsKey("command")){
			agentMeta.setCommand((String)valueMap.get("command"));
		}
		
		if(valueMap.containsKey("testUrl")){
			agentMeta.setTestUrl((String)valueMap.get("testUrl"));
		}
		
		if(valueMap.containsKey("destination")){
			agentMeta.setDestination((String)valueMap.get("destination"));
		}
		
	
	}
	
}
