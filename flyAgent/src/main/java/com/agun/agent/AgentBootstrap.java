package com.agun.agent;

import com.agun.agent.adapter.ServiceType;
import com.agun.agent.adapter.TomcatService;
import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.agent.model.FlyJenkinsInfo;
import com.agun.agent.model.util.ModelAssignUtil;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.ProcessTreeHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
public class AgentBootstrap {
	
	static Logger log = Logger.getLogger(AgentBootstrap.class.getName());
	
	private String agentHost = null;
	
	public AgentBootstrap(String host){
		agentHost = host;
	}
	public CLIHelper auth(String rsaPath, String jenkinsHost){
		CLIHelper cliHelper = new CLIHelper(jenkinsHost, rsaPath);
		return cliHelper;
	}

	public CLIHelper start(String rsaPath, String jenkinsHost){
		log.info("start agent init");
		
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

		
		Map<String, Object> resultMap = cliHelper.callActionFunction("FlyIdentify", "initInfo", null);
		if(resultMap == null){
			log.error("can't obtain flyJenkins info");
			return;
		}
		
		String flyJenkinsHome = (String)resultMap.get("FLY_JENKINS_HOME");
		String jenkinsHome = (String)resultMap.get("JENKINS_HOME");
	
		if(flyJenkinsHome == null || jenkinsHome == null){
			log.error("can't obtain flyJenkins info: " + flyJenkinsHome + "," + jenkinsHome);
			return;
		}
		
		log.debug("get flyjenkins info : " + jenkinsHome +" : "  + flyJenkinsHome);
		FlyJenkinsInfo flyJenkinsInfo = new FlyJenkinsInfo();
		flyJenkinsInfo.setFlyJenkinsHome(flyJenkinsHome);
		flyJenkinsInfo.setJenkinsHome(jenkinsHome);
		AgentMemoryStore agentMemoryStore = AgentMemoryStore.getInstance();
		agentMemoryStore.setFlyJenkinsInfo(flyJenkinsInfo);
		
		resultMap = cliHelper.callActionFunction("FlyIdentify", "identifyAgent", agentHost);
	
		if(resultMap == null){
			log.error("can't identify agent");
			return;
		}
	
		
		/**
		 * agent의 기본 식별 정보를 구한다.
		 */
		int index = 1;
		AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
		for(Entry<String, Object> resultEntry : resultMap.entrySet()){
			Map<String, Object> valueMap =  (Map<String, Object>)resultEntry.getValue();
			valueMap.put("id", index);
			AgentMeta agentMeta = new AgentMeta();
			ModelAssignUtil.assignAgentMeta(agentMeta, valueMap);
			agentMemory.addAgentMeta(agentMeta);
			index++;
		}
		log.info(" agentMeta assign completed : " + index);
	}

	/**
	 * Agent Service 들의 인스턴스를 구분한다. 
	 */
	public void identity(CLIHelper cliHelper){
	
		log.info("start identity service ");
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
					onServerPidMap.put(agentMeta.getServiceId(), pid);
				}
			}
		}
		
		if(onServerPidMap.size() > 0)
			cliHelper.callActionFunction("FlyIdentify", "identify",  onServerPidMap);
		
		instanceModel(cliHelper);

		log.info("identity service completed : " + onServerPidMap.size());
	}
	
	private void instanceModel(CLIHelper cliHelper){
		Map<Integer, String> processMap  = ProcessTreeHelper.refresh();
		processMap.put(0, agentHost);
		cliHelper.callActionFunction("FlyIdentify", "instanceModel",  processMap);
	}
	
	/**
	 * Agent 의 초기화 완
	 */
	public void complete(){
		log.info(" initialization agent completed : ");
	}
}
