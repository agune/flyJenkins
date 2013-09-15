package com.agun.flyJenkins.util;

import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.FlyJenkinsInfo;

public class FlyJenkinsInfoManager {

	public static String getLastBuldInfo(int serviceId){
		AgentMemoryStore agentMeaMemoryStore = AgentMemoryStore.getInstance();
		FlyJenkinsInfo flyJenkinsInfo = agentMeaMemoryStore.getFlyJenkinsInfo();
		String flyJenkinsHome = flyJenkinsInfo.getFlyJenkinsHome();
		return flyJenkinsHome + "/lastBuild/" + serviceId + "/service.zip";
	}
}
