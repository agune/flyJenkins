 package com.agun.flyJenkins.deploy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.agun.flyJenkins.model.DeployMeta;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.DeployRequest;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.job.JobExtends;
import com.agun.flyJenkins.persistence.DeployLogSaveable;
import com.agun.flyJenkins.persistence.DeployRequestSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;


public class DeploySurveillant {
	
	private Map<String, LinkedList<DeployMeta>> deployQueueMap = new Hashtable<String, LinkedList<DeployMeta>>();
	private LinkedList<DeployLog> deployLogQueue = new LinkedList<DeployLog>();
	private Map<String, DeployReport> deployReportMap = new ConcurrentHashMap<String, DeployReport>();
	private Map<String, DeployMeta> deployWorkMap = new Hashtable<String, DeployMeta>();
	/**
	 * 초기화를 할 경우 저장된 deploy log 를 가져 온다. 저장된 deploy log 가 없을때 새로 생성한다. 
	 */
	public DeploySurveillant(){
		DeployLogSaveable deployLogSaveable = new DeployLogSaveable();
		deployLogSaveable.load();
	}
	
	/**
	 * deploy request 요청한 항목중에 DeployLog 로 전환되지 않는 값들을 저장한다.
	 * 
	 */
	public void checkDeploy(Date date){
		DeployRequestSaveable deployRequestSaveable = new DeployRequestSaveable();
		deployRequestSaveable.load();
	   	List<DeployRequest> saveDeployRequestList =deployRequestSaveable.getDeployRequestList();
	   	if(saveDeployRequestList == null)
	   		return;
	   	
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
    	serviceGroupSaveable.load();
    	List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
    	if(serviceGroupList == null)
    		return;
    	boolean isChange = false;
		for(DeployRequest saveDeployRequest : saveDeployRequestList){
			Date reserveDate  = saveDeployRequest.getReserveDate();
			if(saveDeployRequest.isQueue() == false 
					&& saveDeployRequest.isConfirm() 
					&& (reserveDate == null || (reserveDate.getTime() <= date.getTime()))
			   )
			
			{
				extractDeployLog(saveDeployRequest, serviceGroupList);
				isChange = true;
			}
		}
		
		if(isChange){
			try {
				deployRequestSaveable.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
   }
	
	
	
	/**
	 * 주기적으로 deploy 관련 작업을 진행한다. 
	 */
	public void process(){
		Date date = new Date();
		checkDeploy(date);
		nextWorkMap();
 		nextDeploy();
	}
	
	public Map<String, DeployReport> getDeployReportMap(){
		return deployReportMap;
	}
	
	private void nextWorkMap(){
		if(deployLogQueue.size() == 0)
			return;
		addDeployWorkMap(deployLogQueue.remove());
	}
	
	private void nextDeploy(){
		if(deployWorkMap.size() == 0)
			return;
		
		for(Entry<String, DeployMeta> entry : deployWorkMap.entrySet()){
			DeployMeta deployMeta = entry.getValue();
			String key = deployMeta.getDeployId();
			if(deployReportMap.containsKey(key)){
				DeployReport deployReport =  deployReportMap.get(key); 
				int nextOrder = deployReport.getNextOrder();
				
				if(nextOrder == deployMeta.getOrder()){
					DeployMeta selDeployMeta = deployWorkMap.remove(entry.getKey());
					addDeployMetaQueue(selDeployMeta);
				}
			}
		}
	}
	
	private void addDeployWorkMap(DeployLog deployLog){
		if(deployLog == null)
			return;
		
		DeployMeta deployMeta = new DeployMeta();
		deployMeta.setDate(new Date());
		deployMeta.setDeployId(deployLog.getDeployId());
		deployMeta.setGroupId(deployLog.getServiceGroupId());
		deployMeta.setHost(deployLog.getHost());
		deployMeta.setJobName(deployLog.getJobName());
		deployMeta.setOrder(deployLog.getRequestOrder());
		deployMeta.setProduction(deployLog.getProduction());
		deployMeta.setServiceId(deployLog.getServiceId());
		
		deployWorkMap.put(deployMeta.getDeployId()+deployMeta.getOrder(), deployMeta);
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
	 * deployLog list 에 deployLog 를 추가
	 * @param deployRequest
	 */
	private void addDeployLog(DeployRequest deployRequest, ServiceMeta serviceMeta, int order){
		DeployLog deployLog = new DeployLog();
		deployLog.setDeployId(deployRequest.getJobName() + deployRequest.getDate().getTime());
		deployLog.setJobName(deployRequest.getJobName());
		deployLog.setServiceGroupId(deployRequest.getServerGroup());
		deployLog.setDate(deployRequest.getDate());
		deployLog.setServiceId(serviceMeta.getServiceId());
		deployLog.setRequestOrder(order);
		deployLog.setHost(serviceMeta.getHost());
		deployLog.setReserveDate(deployRequest.getReserveDate());
		deployLog.setProduction(deployRequest.getProduction());
		deployLogQueue.add(deployLog);
	}
	
	
	private void extractDeployLog(DeployRequest deployRequest, List<ServiceGroup> serviceGroupList){
		int order = 0;
		for(ServiceGroup serviceGroup :  serviceGroupList){
			if(deployRequest.getServerGroup() == serviceGroup.getGroupId()){
				List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
	
				order = 1;
				for(ServiceMeta serviceMeta : serviceMetaList){
					addDeployLog(deployRequest, serviceMeta, order);
					order++;
				}
				
				DeployReport deployReport = new DeployReport();
				deployReport.setDeployId(deployRequest.getJobName() + deployRequest.getDate().getTime());
				deployReport.setDeploySize(serviceMetaList.size());
				deployReportMap.put(deployReport.getDeployId(), deployReport);
				break;
			}
		}
		deployRequest.setQueue(true);
	}
}
