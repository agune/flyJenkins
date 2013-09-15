package com.agun.flyJenkins.process;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jenkins.model.Jenkins;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.AgentService;
import com.agun.flyJenkins.model.InstanceModel;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.model.util.ConvertUtil;
import com.agun.flyJenkins.network.NetworkSpace;

public class FlyIdentify implements FlyProcess {
	
	private static FlyIdentify flyIdentify = new FlyIdentify();
	
	private FlyIdentify(){}
	
	public static FlyIdentify getInstance(){
		return flyIdentify;
	}
	
	public Map<String, Object> getInitInfo(){
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		resultMap.put("JENKINS_HOME", Jenkins.getInstance().getRootDir().getAbsolutePath());
		resultMap.put("FLY_JENKINS_HOME", Jenkins.getInstance().getRootDir().getAbsolutePath() + "/flyJenkins");
		return resultMap;
	}
	
	
	
	/**
	 * 구성된 서버의 instance 를 식별한다. 
	 * @param Map<Integer, Integer>
	 */
	
	public void identify(Map<Integer, Integer> servicePidMap){
		NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
		Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
		
		synchronized (networkMap) {
			for(List<AgentService> agentList :  networkMap.values()){
				for(AgentService agentService :  agentList){
					if(agentService.getServiceMetaList() !=null){
						for(ServiceMeta serviceMeta : agentService.getServiceMetaList()){
							if(servicePidMap.containsKey(serviceMeta.getServiceId())){
								serviceMeta.setPid(servicePidMap.get(serviceMeta.getServiceId()));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * instance model 을 생성한다.
	 * @param argMap
	 */
	public void instanceModel(Map<Integer, String> argMap){
		
		NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
		Map<String, List<InstanceModel>> instanceMap =  networkSpace.getInstanceModelMap();

		String host  = argMap.get(0);
		
		instanceMap.remove(host);
		List<InstanceModel> instanceModelList = new ArrayList<InstanceModel>();
		for(Entry<Integer, String> entry : argMap.entrySet()){
			if(entry.getKey() > 0){
				InstanceModel newInstanceModel = new InstanceModel();
				newInstanceModel.setPid(entry.getKey());
				newInstanceModel.setHost(host);
				newInstanceModel.setArgs(entry.getValue());
				instanceModelList.add(newInstanceModel);
			}
		}
		instanceMap.put(host, instanceModelList);
	}
	
	/**
	 * agent에서 식별 정보 요청을 받아 준다.
	 * @param host
	 * @return
	 */
	
	public Map<String, Object> getIdentifyAgent(String host){
		NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
		List<AgentService> agentServiceList = networkSpace.getAgentListByHost(host);
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		for(AgentService agentService :  agentServiceList){
			if(agentService.getServiceMetaList() != null){
				for(ServiceMeta serviceMeta : agentService.getServiceMetaList())
					resultMap.put(serviceMeta.getServiceId().toString(), ConvertUtil.getMapFromServiceMeta(serviceMeta));
			}
		}
		return (Map<String, Object>)resultMap;
	}
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("initInfo".equals(operName)){
			return getInitInfo();
		}else if("identifyAgent".equals(operName)){
			return getIdentifyAgent((String)arg1);
		}else if("identify".equals(operName)){
			identify((Map<Integer, Integer>)arg1);
		}else if("instanceModel".equals(operName)){
			instanceModel((Map<Integer, String>)arg1);
		}
		return null;
	}

}
