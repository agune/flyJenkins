package com.agun.system;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;

public class AgentInfoManager {

	public static String getAgnetHome(){
		String agentHome = System.getenv("FLY_AGENT_HOME");
		if(agentHome == null)
			return ".";
		return agentHome;
	}
	
	public static String getProductionPath(int serverId, String productionPath){
		return AgentInfoManager.getAgnetHome() + "/production/" + serverId + "/" + productionPath; 
	}
	
	public static void checkProductionDir(int serverId){
		String tagret = AgentInfoManager.getAgnetHome() + "/production/" + serverId;
		FilePath filePath = new FilePath(new File(tagret));
		try {
			if(filePath.exists() == false){
				filePath.mkdirs();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int getAgentMaxId(){
		AgentMemoryStore agentMemoryStore = AgentMemoryStore.getInstance();
		List<AgentMeta> agentMetaList = agentMemoryStore.getAgentMetaList();
		int maxId = 0;
		for(AgentMeta agentMeta : agentMetaList){
			if(maxId < agentMeta.getId())
				maxId = agentMeta.getId();
		}
		maxId++;
		return maxId;
	}
	
	
	public static String checkAgentWorkingDir(){
		String target =  AgentInfoManager.getAgnetHome() + "/work";
		FilePath filePath = new FilePath(new File(target));
		try {
			if(filePath.exists() == false){
				filePath.mkdirs();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return target;
	}
	public static String checkProductionLastDeployDir(int serviceId){
		String target = AgentInfoManager.getAgnetHome() + "/production/" + serviceId + "/last";
		FilePath filePath = new FilePath(new File(target));
		try {
			if(filePath.exists() == false){
				filePath.mkdirs();
			}
			 return target;
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
