package com.agun.system;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
