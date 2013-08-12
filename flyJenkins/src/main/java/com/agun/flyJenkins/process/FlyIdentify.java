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
	
	
	/**
	 * 구성된 서버의 instance 를 식별한다. 
	 * @param Map<Integer, Integer>
	 */
	
	public void identify(Map<Integer, Integer> serverPidMap){
		NetworkSpace networkSpace  = NetworkSpace.getInstance();
		Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
		
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		for(List<AgentService> agentList :  networkMap.values()){
			for(AgentService agent :  agentList){
				ServiceGroup serverGroup = agent.getServiceGroup();
				for(ServerMeta serverMeta : serverGroup.getServerMetaList()){
					if(serverPidMap.containsKey(serverMeta.getServerId())){
						serverMeta.setPid(serverPidMap.get(serverMeta.getServerId()));
						break;
					}
				}
			}
		}
		
	}
	
	/**
	 * agent에서 식별 정보 요청을 받아 준다.
	 * @param host
	 * @return
	 */
	
	public Map<String, Object> getIdentifyAgent(String host){
		NetworkSpace networkSpace  = NetworkSpace.getInstance();
		List<AgentService> agentList = networkSpace.getAgentList(host);
		
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		for(AgentService agent :  agentList){
			resultMap.put("agentId", agent.getAgentId());
			ServiceGroup serverGroup = agent.getServiceGroup();
			for(ServerMeta serverMeta : serverGroup.getServerMetaList())
				resultMap.put(serverMeta.getServerId().toString(), serverMeta.convertMap());
		}
		return (Map<String, Object>)resultMap;
	}
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("identifyAgent".equals(operName)){
			return getIdentifyAgent((String)arg1);
		}else if("identify".equals(operName)){
			identify((Map<Integer, Integer>)arg1);
		}
		return null;
	}

}
