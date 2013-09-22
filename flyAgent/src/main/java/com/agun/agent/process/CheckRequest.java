package com.agun.agent.process;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.agun.agent.AgentBootstrap;
import com.agun.agent.adapter.AdapterFactory;
import com.agun.agent.adapter.ServiceType;
import com.agun.agent.function.CommonFunction;
import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.agent.model.util.ModelAssignUtil;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;

public class CheckRequest {

	static Logger log = Logger.getLogger(CheckRequest.class.getName());
	
	private CLIHelper cliHelper;
	private FilePathHelper filePathHelper;
	private String agentHost = null;
	
	
	public CheckRequest(CLIHelper cliHelper, FilePathHelper filePathHelper, String host){
		this.agentHost = host;
		this.cliHelper = cliHelper; 
		this.filePathHelper = filePathHelper;
	}

	public void process(List<AgentMeta> agentList){
	
		if(agentList.size() > 0){
			identity(agentList);
			instanceModel();
		}
		
		Map<String, Object> resultMap = null;
		resultMap = cliHelper.callActionFunction("FlyRequester", "peekRequest", agentHost);
		if(resultMap == null 
				|| resultMap.containsKey("type") == false
				|| resultMap.containsKey("arg") == false)
			return;
		
		int type = (Integer)resultMap.get("type");
		// stop process
		if(type ==  1){
			 Map<String, Object> argMap = (Map<String, Object>)resultMap.get("arg");
			
			 if(argMap.containsKey("pid") == false)
				return;
			int pid = (Integer)argMap.get("pid");
			for(AgentMeta agent : agentList){
				if(agent.getPid() == pid){
					ServiceType serviceType = AdapterFactory.getServiceType(agent, null);
					serviceType.stop(agent);
					break;
				}
			}
		
			// Copy Service
		}else if(type == 2){
			 Map<String, Object> argMap = (Map<String, Object>)resultMap.get("arg");
			 if(argMap.containsKey("serviceType") == false 
					 || argMap.containsKey("production") == false
					 || argMap.containsKey("destination") == false
					 ){
				 log.error("fail copy service info: " + argMap);
				 return;
			 }
			log.info("start copy service : " + argMap.get("serviceType") + "," + argMap.get("production") + "," + argMap.get("destination") );
			CommonFunction.copyService((Integer)argMap.get("serviceType"), (String)argMap.get("production"), (String)argMap.get("destination") , filePathHelper);
		
		// attach service
		}else if(type == 3){
			int maxId = AgentInfoManager.getAgentMaxId();
			Map<String, Object> argMap = (Map<String, Object>)resultMap.get("arg");
			argMap.put("id", maxId);
			AgentMeta agentMeta = new AgentMeta();
			ModelAssignUtil.assignAgentMeta(agentMeta, argMap);
			AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
			agentMemory.addAgentMeta(agentMeta);	
		
			AgentMeta debugAgentMeta = agentMemory.getAgentMeta(agentMeta.getId());
			log.info(" attach service meta : " + agentMeta.getId() + "," + agentMeta.getServiceId() + "," + agentMemory.getAgentTotalSize());
		
		// delete service
		}else if(type == 4){
			
			Map<String, Object> argMap = (Map<String, Object>)resultMap.get("arg");
			if(argMap == null)
				return;
			int serviceId = (Integer)argMap.get("serviceId");
			AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
			agentMemory.delAgentMeta(serviceId);
			log.info(" delete service meta " + serviceId + "," + agentMemory.getAgentTotalSize() );
		}
	}
	
	/**
	 * 서버 식별 정보를 다시 보내 준다.
	 * @param agentList
	 */
	private void identity(List<AgentMeta> agentList){
		Map<Integer, Integer> onServerPidMap = new Hashtable<Integer, Integer>();
		for(AgentMeta agent :  agentList){
			ServiceType serviceType = AdapterFactory.getServiceType(agent, null);
			serviceType.getPid(agent);
			onServerPidMap.put(agent.getServiceId(), agent.getPid());
		}
		if(onServerPidMap.size() > 0)
			cliHelper.callActionFunction("FlyIdentify", "identify",  onServerPidMap);
	}
	
	
	public void checkDeploy(){
		Map<String, Object> resultMap = null;
		resultMap = cliHelper.callActionFunction("FlyDeploy", "deployInfo", agentHost);
		if(resultMap == null || 
				resultMap.containsKey("production") == false 
				|| resultMap.containsKey("serverId") == false
				|| resultMap.containsKey("deployId") == false
			)
			return;
		
		String result = (String) resultMap.get("production");
		String deployId = (String)resultMap.get("deployId");
		int serverId = (Integer) resultMap.get("serverId");
		
		log.info("start deployment: " + deployId + " to " + result);
		
		AgentMemoryStore agentMemoryStore = AgentMemoryStore.getInstance();
		List<AgentMeta> agentMetaList = agentMemoryStore.getAgentMetaList();
		ServiceType service = null;
		for(AgentMeta agentMeta : agentMetaList){
			if(agentMeta.getServiceId() == serverId){
				service = AdapterFactory.getServiceType(agentMeta, filePathHelper);
				service.stop(agentMeta);
				service.getProduction(agentMeta, result);
				boolean deployOk = service.deploy(agentMeta);
				if(deployOk){
					deployOk = service.monitoring(agentMeta);
					if(deployOk){
						service.complete(agentMeta);
						completeDeploy(agentMeta, deployId);
					}else{
						failDeploy(agentMeta, deployId);
					}
				}
				break;
			}
		}
	}
	
	private void completeDeploy(AgentMeta agentMeta, String deployId){
		log.info(" deployment completed : " + deployId);
		cliHelper.callActionFunction("FlyDeploy", "deployComplete", deployId);
	}
	
	private void failDeploy(AgentMeta agentMeta, String deployId){
		log.info(" deployment failure : " + deployId);
		cliHelper.callActionFunction("FlyDeploy", "deployFail", deployId);
	}
	
	/**
	 *	서버에 프로세스 정보를 전달해 준다.  
	 */
	private void instanceModel(){
		Map<Integer, String> processMap  = ProcessTreeHelper.refresh();
		processMap.put(0, agentHost);
		cliHelper.callActionFunction("FlyIdentify", "instanceModel",  processMap);
	}
	
}
