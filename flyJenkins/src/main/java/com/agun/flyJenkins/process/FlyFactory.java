package com.agun.flyJenkins.process;

import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * @author agun
 */


public class FlyFactory {

	static Map<String, FlyProcess> processMap = new Hashtable<String, FlyProcess>();
	
	static{
		processMap.put("FlyDeploy", FlyFactory.getFlyDeploy());
		processMap.put("FlyIdentify", FlyFactory.getFlyIdentify());
	}
	/**
	 * FlyFactory 는 직접 생성하면 안된다.
	 */
	private FlyFactory(){
		
	}
	
	public static FlyProcess getProcess(String processName){
		
		if(processMap.containsKey(processName))
			return processMap.get(processName);
		
		return null;
	}
	public static FlyIdentify getFlyIdentify(){
		return FlyIdentify.getInstance();
	}
	
	public static FlyDeploy getFlyDeploy(){
		return FlyDeploy.getInstance();
	}
}
