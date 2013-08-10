package com.agun.system;

public class AgentInfoManager {

	public static String getAgnetHome(){
		return "E:\\test";
	}
	
	public static String getProductionPath(int agentId){
		return AgentInfoManager.getAgnetHome() + "/production/" + agentId + "/product.jar"; 
	}
}
