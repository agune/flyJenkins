package com.agun.flyJenkins.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.model.DeployMeta;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.network.NetworkSpace;
import com.agun.flyJenkins.persistence.DeployLogSaveable;
import com.agun.flyJenkins.persistence.DeployMetaSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;
import com.agun.flyJenkins.deploy.DeploySurveillant;
import com.agun.flyJenkins.schedule.PeriodWork;


/**
 * deploy 관련된 기능을 제공하는 Action Class 
 * @author agun
 *
 */

public class FlyDeploy implements FlyProcess{
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
	public Map<String, Object> getDeployInfo(String host){
		PeriodWork periodWork = com.agun.flyJenkins.FlyFactory.getPeriodWork();
		DeploySurveillant deploySurveillant =  periodWork.getDeploySurveillant();
		DeployMeta deployMeta = deploySurveillant.peekDeployMeta(host);
		if(deployMeta == null)
			return Collections.EMPTY_MAP;
		
		saveDeployMeta(deployMeta);
		
		System.out.println("deploy info ====>" + deployMeta.getProduction());
		
		Map<String, Object> deployInfoMap = new Hashtable<String, Object>();
		deployInfoMap.put("production", deployMeta.getProduction());
		deployInfoMap.put("serverId", deployMeta.getServiceId());
		deployInfoMap.put("deployId", deployMeta.getDeployId());
		deployInfoMap.put("order", deployMeta.getOrder());
		
		return deployInfoMap;
	}

	
	public void deployComplete(String deployId){
		Map<String, DeployReport> deployReportMap = FlyFactory.getPeriodWork().getDeployReportMap();
		
		if(deployReportMap.containsKey(deployId)){
			DeployReport deployReport = deployReportMap.get(deployId);
			deployReport.plusSuccessCount();
			
			serviceComplete(deployId);
		}
	}
	
	
	public void deployFail(String deployId){
		Map<String, DeployReport> deployReportMap = FlyFactory.getPeriodWork().getDeployReportMap();
		if(deployReportMap.containsKey(deployId)){
			DeployReport deployReport = deployReportMap.get(deployId);
			deployReport.plusFailCount();
		}
	}
	
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("deployInfo".equals(operName)){
			return this.getDeployInfo((String)arg1);
		}else if("deployComplete".equals(operName)){
			this.deployComplete((String) arg1);
		}else if("deployFail".equals(operName)){
			this.deployFail((String) arg1);
		}
		return null;
	}
	
	private void serviceComplete(String deployId){
		DeployMetaSaveable deployMetaSaveable = new DeployMetaSaveable();
		deployMetaSaveable.load();
		
		List<DeployMeta> deployMetaList = deployMetaSaveable.getDeployMetaList();
		
		System.out.println("====> service complete1 ");
		if(deployMetaList != null){
			System.out.println("====> service complete2 ");
			for(DeployMeta deployMeta : deployMetaList){
				if(deployMeta.getDeployId().equals(deployId)){
					saveInstallAble(deployMeta.getServiceId());
					break;
				}
			}
		}
	}
	
	private void saveInstallAble(int serviceId){
		
		System.out.println("=====> start save saveInstallAble ");
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
		if(serviceGroupList == null)
			return;
		
		boolean isService = false;
		
		for(ServiceGroup serviceGroup : serviceGroupList){
			List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
			for(ServiceMeta serviceMeta : serviceMetaList){
				if(serviceMeta.getServiceId() == serviceId){
					if(serviceMeta.isInstallAble() == false){
						serviceMeta.setInstallAble(true);
						isService = true;
					}
					break;
				}
			}
		}
		System.out.println("=====> end save " + isService);
		
		
		if(isService){
			try {
				serviceGroupSaveable.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
			networkSpace.reload();
		}
	}
	
	private void saveDeployMeta(DeployMeta deployMeta){
		DeployMetaSaveable deployMetaSaveable = new DeployMetaSaveable();
		deployMetaSaveable.load();
		List<DeployMeta> deployMetaList = deployMetaSaveable.getDeployMetaList();
		if(deployMetaList == null)
			deployMetaList = new ArrayList<DeployMeta>();
		
		deployMetaList.add(deployMeta);
		deployMetaSaveable.setDeployMetaList(deployMetaList);
		
		try {
			deployMetaSaveable.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
