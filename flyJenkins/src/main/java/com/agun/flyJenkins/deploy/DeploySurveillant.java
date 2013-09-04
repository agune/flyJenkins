 package com.agun.flyJenkins.deploy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.job.JobExtends;
import com.agun.flyJenkins.persistence.DeployLogSaveable;
import com.agun.flyJenkins.persistence.DeployRequestSaveable;
import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.service.ServerMeta;
import com.agun.flyJenkins.service.ServiceGroup;

public class DeploySurveillant {
	List<DeployLog> deployLogList;
	
	private Map<String, LinkedList<DeployMeta>> deployQueueMap = new Hashtable<String, LinkedList<DeployMeta>>();
	
	/**
	 * 초기화를 할 경우 저장된 deploy log 를 가져 온다. 저장된 deploy log 가 없을때 새로 생성한다. 
	 */
	public DeploySurveillant(){
		DeployLogSaveable deployLogSaveable = new DeployLogSaveable();
		deployLogSaveable.load();
		deployLogList = deployLogSaveable.getDeployLogList();
		
		if(deployLogList == null)
			deployLogList = new ArrayList<DeployLog>();
	}
	
	/**
	 * deploy request 요청한 항목중에 DeployLog 로 전환되지 않는 값들을 저장한다.
	 * 
	 */
	public void checkDeploy(){
		DeployRequestSaveable deployRequestSaveable = new DeployRequestSaveable();
		deployRequestSaveable.load();
    	
    	List<DeployRequest> saveDeployRequestList =deployRequestSaveable.getDeployRequestList();
    	if(saveDeployRequestList != null){
    		for(DeployRequest saveDeployRequest : saveDeployRequestList){
    			if(checkDeployLog(saveDeployRequest)){
    				addDeployLog(saveDeployRequest);
    			}
    		}
    	}
	}
	
	
	public List<DeployLog> getDeployLogList(){
		return this.deployLogList;
	}
	
	/**
	 * 주기적으로 deploy 관련 작업을 진행한다. 
	 */
	public void process(){
		
		checkDeploy();
    	DeployLog lastDeployLog = null;
		for(DeployLog deployLog : deployLogList){
			 Map<String, ServerMeta> weightMap = checkPriority(deployLog);
			createDeployMeta(deployLog,  weightMap);
			nextDeploy(deployLog,  weightMap);
			lastDeployLog = deployLog;
		}
		if(lastDeployLog == null)
			return;
		
		/**
		 * jenkins fail over 시 상태값을 저장하기 위하여 저장한다.
		 * 
		 */
		DeployLogSaveable deployLogSaveable = new DeployLogSaveable();
		
		deployLogSaveable.setDeployLogList(deployLogList);
		try {
			deployLogSaveable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Service Group 의 배포 우선순위를 구한다. 
	 * @param deployLog
	 */
	private  Map<String, ServerMeta> checkPriority(DeployLog deployLog){
		NetworkSpace networkSpace = NetworkSpace.getInstance();
		Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
	
		if(networkMap == null)
			return Collections.EMPTY_MAP;
		boolean isGroup = false;
		Map<String, ServerMeta> weightMap = new TreeMap<String, ServerMeta>();
		for(List<AgentService> agentList : networkMap.values()){
			for(AgentService agent : agentList){
				ServiceGroup serviceGroup = agent.getServiceGroup();
				
				if(serviceGroup.getGroupId() == deployLog.getGroupId()){
					List<ServerMeta> serverList = serviceGroup.getServerMetaList();
					isGroup = true;
					for(ServerMeta serverMeta: serverList){
						weightMap.put(serverMeta.getWeight() + "_" + serverMeta.getServerId()  , serverMeta);
					}
					break;
				}
			}
			if(isGroup){
				break;
			}
		}
		return weightMap;
	}
	
	/**
	 * Deploy Log 를 기준으로 Deploy Meta 를 생성한다. 
	 * @param deployLog
	 * @param weightMap
	 */
	private void createDeployMeta(DeployLog deployLog, Map<String, ServerMeta> weightMap){
		if(deployLog.getStepOrder() > 0)
			return;
		deployLog.setStepOrder(1);
		deployLog.setWorkSize(weightMap.size());
		for(String key : weightMap.keySet()){
			ServerMeta serverMeta = weightMap.get(key);
			DeployMeta deployMeta = new DeployMeta();
			deployMeta.setDate(deployLog.getDate());
			deployMeta.setGroupId(serverMeta.getGroupId());
			deployMeta.setHost(serverMeta.getHost());
			deployMeta.setJobName(deployLog.getJobName());
			deployMeta.setProduction(deployLog.getProduction());
			deployMeta.setServerId(serverMeta.getServerId());
			addDeployMetaQueue(deployMeta);
			break;
		}
	}
	
	private void nextDeploy(DeployLog deployLog, Map<String, ServerMeta> weightMap){
		if(deployLog.getCompleteCount() != deployLog.getStepOrder() 
		   || deployLog.getWorkSize() <= deployLog.getStepOrder())
			return;
		int stepOrder = deployLog.getStepOrder();
		stepOrder++;
		deployLog.setStepOrder(stepOrder);
		
		int count = 1;
		for(String key : weightMap.keySet()){
			if(count == stepOrder){
				ServerMeta serverMeta = weightMap.get(key);
				DeployMeta deployMeta = new DeployMeta();
				deployMeta.setDate(deployLog.getDate());
				deployMeta.setGroupId(serverMeta.getGroupId());
				deployMeta.setHost(serverMeta.getHost());
				deployMeta.setJobName(deployLog.getJobName());
				deployMeta.setProduction(deployLog.getProduction());
				deployMeta.setServerId(serverMeta.getServerId());
				addDeployMetaQueue(deployMeta);
				break;
			}
			count++;
		}
	}
	
	/**
	 * deployMeta 를 queue 에 저장 하여 deploy 가 진행 되게 한다. 
	 * @param deployMeta
	 */
	public void addDeployMetaQueue(DeployMeta deployMeta){
		if(deployQueueMap.containsKey(deployMeta.getHost())){
			LinkedList<DeployMeta> deployMetaQueue = deployQueueMap.get(deployMeta.getHost());
			deployMetaQueue.add(deployMeta);
		}else{
			LinkedList<DeployMeta> deployMetaQueue = new LinkedList<DeployMeta>();
			deployMetaQueue.add(deployMeta);
			deployQueueMap.put(deployMeta.getHost(), deployMetaQueue);
		}
	}
	/**
	 * deploy queue 에서 deploy meta 를 가져온다.
	 * @param host
	 * @return DeployMeta
	 */
	public DeployMeta peekDeployMeta(String host){
		if(deployQueueMap.containsKey(host) == false){
			return null;
		}
		LinkedList<DeployMeta> deployQueue = deployQueueMap.get(host);
		
		if(deployQueue.size() == 0)
			return null;
		
		return deployQueue.remove();
	}
	
	/**
	 * job name 을 기준으로 production 정보를 구
	 * @param deployLog
	 */
	private void getJobProduction(DeployLog deployLog){
		Map<String, Object> resultMap = FlyFactory.getPropertiesOfJob(deployLog.getJobName());
		for(Entry entry : resultMap.entrySet()){
    		if(entry.getValue() instanceof JobExtends){
    			JobExtends jobExtend = (JobExtends) entry.getValue();
    			deployLog.setProduction(jobExtend.production);
    			return;
    		}
    	}
   }
	/**
	 * deployLog list 에 deployLog 를 추가
	 * @param deployRequest
	 */
	private void addDeployLog(DeployRequest deployRequest){
		DeployLog deployLog = new DeployLog();
		deployLog.setJobName(deployRequest.getJobName());
		deployLog.setGroupId(deployRequest.getServerGroup());
		deployLog.setDate(deployRequest.getDate());
		deployLog.setStepOrder(0);
		getJobProduction(deployLog);
		synchronized (deployLogList) {
			deployLogList.add(deployLog);
		}
	}
	/**
	 * deploy request 가 deploy log 로 존재하는지 체크 
	 * @param deployRequest
	 * @return
	 */
	private boolean checkDeployLog(DeployRequest deployRequest){
		// confirm 이 안된건 제외한다.
		if(deployRequest.isConfirm() == false)
			return false;
		for(DeployLog deployLog : deployLogList){
			if(deployRequest.getDate().getTime() == deployLog.getDate().getTime()
				&& deployRequest.getJobName() == deployLog.getJobName()){
				return false;
			}
		}
		return true;
	}
}
