package com.agun.flyJenkins.process;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.DeployMeta;
import com.agun.flyJenkins.model.DeployReport;
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
		}
	}
	
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("deployInfo".equals(operName)){
			return this.getDeployInfo((String)arg1);
		}else if("deployComplete".equals(operName)){
			this.deployComplete((String) arg1);
		}
		return null;
	}
}
