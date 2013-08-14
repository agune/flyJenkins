package com.agun.agent;

import com.agun.agent.adapter.ServiceType;
import com.agun.agent.adapter.TomcatService;
import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.ProcessTreeHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class AgentBootstrap {
	
	public CLIHelper auth(String rsaPath, String jenkinsHost){
		CLIHelper cliHelper = new CLIHelper(jenkinsHost, rsaPath);
		return cliHelper;
	}

	public CLIHelper start(String rsaPath, String jenkinsHost){
		CLIHelper cliHelper = auth(rsaPath, jenkinsHost);
		init(cliHelper);
		identity(cliHelper);
		return cliHelper;
	}
	
	/**
	 * Agent를 초기화 한다.
	 * @param cliHelper
	 */
	public void init(CLIHelper cliHelper){
		try {
			
			System.out.println(InetAddress.getLocalHost().getHostName());
	//		Map<String, Object> resultMap = cliHelper.callActionFunction("FlyIdentify", "identifyAgent", InetAddress.getLocalHost());
			Map<String, Object> resultMap = cliHelper.callActionFunction("FlyIdentify", "identifyAgent", "127.0.0.1");
		
			if(resultMap == null)
				return;
		
			/**
			 * agent의 기본 식별 정보를 구한다.
			 */
			AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
			for(Entry<String, Object> resultEntry : resultMap.entrySet()){
				AgentMeta agentMeta = new AgentMeta();
				if(resultEntry.getKey().equals("agentId") == false){
					Map<String, Object> valueMap =  (Map<String, Object>)resultEntry.getValue();
					for(Entry<String, Object> entry : valueMap.entrySet()){
						if("destination".equals(entry.getKey())){
							agentMeta.setDestination((String)entry.getValue());
						}else if("type".equals(entry.getKey())){
							agentMeta.setType((Integer)entry.getValue());
						}else if("testCmd".equals(entry.getKey())){
							agentMeta.setTestCmd((String)entry.getValue());
						}else if("serverId".equals(entry.getKey())){
							agentMeta.setServerId((Integer)entry.getValue());
						}
					}
				}else{
					agentMeta.setId((Integer)resultEntry.getValue());
				}
				agentMemory.addAgentMeta(agentMeta);
				
			}
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Agent Service 들의 인스턴스를 구분한다. 
	 */
	public void identity(CLIHelper cliHelper){
		AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
		List<AgentMeta> agentMetaList = agentMemory.getAgentMetaList();
		Map<Integer, Integer> onServerPidMap = new Hashtable<Integer, Integer>();
		
		for(AgentMeta agentMeta : agentMetaList){
			ServiceType serviceType = null;
			
			if(agentMeta.getType() == 2){
				serviceType = new TomcatService();
			
				int pid = serviceType.getPid(agentMeta);
				if(pid > 0){
					agentMeta.setPid(pid);
					onServerPidMap.put(agentMeta.getServerId(), pid);
				}
			}
		}
		
		if(onServerPidMap.size() > 0)
			cliHelper.callActionFunction("FlyIdentify", "identify",  onServerPidMap);
		
		instanceModel(cliHelper);
		
	}
	
	private void instanceModel(CLIHelper cliHelper){
		Map<Integer, String> processMap  = ProcessTreeHelper.refresh();
		
		processMap.put(0, "127.0.0.1");
		cliHelper.callActionFunction("FlyIdentify", "instanceModel",  processMap);
	}
	
	/**
	 * Agent 의 초기화 완
	 */
	public void complete(){
		
	}
}
