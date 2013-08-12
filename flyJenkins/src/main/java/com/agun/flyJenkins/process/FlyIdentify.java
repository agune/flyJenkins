package com.agun.flyJenkins.process;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.service.ServerMeta;
import com.agun.flyJenkins.service.ServiceGroup;

public class FlyIdentify implements FlyProcess {
	
	private static FlyIdentify flyIdentify = new FlyIdentify();
	
	private FlyIdentify(){
		
	}
	
	public static FlyIdentify getInstance(){
		return flyIdentify;
	}
	
	public Map<String, Object> getIdentifyAgent(String host){
		System.out.println("==========> start inti? ");
		
		NetworkSpace networkSpace  = NetworkSpace.getInstance();
		List<AgentService> agentList = networkSpace.getAgentList(host);
		
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		for(AgentService agent :  agentList){
			
			ServiceGroup serverGroup = agent.getServiceGroup();
			for(ServerMeta serverMeta : serverGroup.getServerMetaList())
				resultMap.put(serverMeta.getServerId().toString(), serverMeta.convertMap());
		}
		return (Map<String, Object>)resultMap;
	}
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("identifyAgent".equals(operName)){
			return getIdentifyAgent((String)arg1);
		}
		return null;
	}

}
