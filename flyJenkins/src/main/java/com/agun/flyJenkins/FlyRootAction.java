/**
 * flyJenkins 
 */
package com.agun.flyJenkins;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.agun.flyJenkins.process.FlyDeploy;
import com.agun.flyJenkins.process.FlyFactory;
import com.agun.flyJenkins.process.FlyProcess;
import com.agun.flyJenkins.service.ServerMeta;
import com.agun.flyJenkins.ui.FlyUI;

import hudson.Extension;
import hudson.model.RootAction;

@Extension
public class FlyRootAction implements RootAction {

	public String getIconFileName() {
		return "gear.png";
	}

	public String getDisplayName() {
		return "flyJenkins";
	}

	public String getUrlName() {
		return "flyJenkins";
	}
	
	 public List<FlyUI> getAll() {
	        return FlyUI.all();
	  }
	
	 public FlyUI getDynamic(String name) {
	        for (FlyUI ui : getAll())
	            if (ui.getUrlName().equals(name))
	                return ui;
	        return null;
	    }
	
	 /**
	  * flyjenkins 의 process 를  구현
	  * @param processName
	  * @return
	  */
	 public FlyProcess getFlyProcess(String processName){
		 return FlyFactory.getProcess(processName);
	 }
	 
	/**
	 * issue #8 구현을 위한 read 메소
	 * @return
	 */
	public Map<Integer, Map<String, Object>> readServerMetaData(){
		Map<Integer, Map<String, Object>> serverMetaMap = new Hashtable<Integer, Map<String,Object>>();
		
		
		ServerMeta serverMeta = new ServerMeta();
		serverMeta.setGroupId(1);
		serverMeta.setHost("127.0.0.1");
		serverMeta.setDestination("/Users/pdc222/jenkins1");
		serverMeta.setServerId(1);
		serverMeta.setTestCmd("");
		serverMeta.setType(1);
		serverMeta.setWeight(1);
	
		serverMetaMap.put(serverMeta.getServerId(), serverMeta.convertMap());

		
		serverMeta.setGroupId(1);
		serverMeta.setHost("127.0.0.1");
		serverMeta.setDestination("/Users/pdc222/jenkins2");
		serverMeta.setServerId(2);
		serverMeta.setTestCmd("");
		serverMeta.setType(1);
		serverMeta.setWeight(1);

		serverMetaMap.put(serverMeta.getServerId(), serverMeta.convertMap());

	
		serverMeta.setGroupId(2);
		serverMeta.setHost("127.0.0.2");
		serverMeta.setDestination("/Users/pdc222/jenkins1");
		serverMeta.setServerId(1);
		serverMeta.setTestCmd("");
		serverMeta.setType(1);
		serverMeta.setWeight(1);

		serverMetaMap.put(serverMeta.getServerId(), serverMeta.convertMap());

		serverMeta.setGroupId(2);
		serverMeta.setHost("127.0.0.2");
		serverMeta.setDestination("/Users/pdc222/jenkins2");
		serverMeta.setServerId(2);
		serverMeta.setTestCmd("");
		serverMeta.setType(1);
		serverMeta.setWeight(1);

		
		return serverMetaMap;
	}
	
	/**
	 * issue #9 를 위한 임시 저장 모듈 
	 * @param processList
	 */
	public void saveProcessList(Hashtable<Integer, String> processInfoMap){
		System.out.println("==============> start test : ");
		try{
			for(Entry<Integer, String> entry : processInfoMap.entrySet()){
				
				System.out.println("process List =====> " + entry.getKey()  + ", name : " + entry.getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
	}
}
