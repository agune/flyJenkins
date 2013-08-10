package com.agun.flyJenkins;

import java.util.Hashtable;
import java.util.Map;


import hudson.Extension;
import hudson.model.Action;

/**
 * deploy 관련된 기능을 제공하는 Action Class 
 * @author agun
 *
 */

public class FlyDeploy {
	private static FlyDeploy flyDeploy = new FlyDeploy();
	
	private FlyDeploy() {
	}

	public static FlyDeploy getInstance(){
		return flyDeploy;
	}
	
	
	/**
	 *  배포에 대한 정보를 제공한다.
	 *  
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getDeployInfo(int agentId){
		System.out.println("===========> getDeployInfo");
		Map<String, Object> deployInfoMap = new Hashtable<String, Object>();
		deployInfoMap.put("production", "E:\\data\\com.danawa.app.cas-1.0-SNAPSHOT.war");
		return deployInfoMap;
	}
}
