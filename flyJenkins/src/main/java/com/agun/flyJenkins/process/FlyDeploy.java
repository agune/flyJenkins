package com.agun.flyJenkins.process;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;














import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.deploy.DeployLog;
import com.agun.flyJenkins.deploy.DeployMeta;
import com.agun.flyJenkins.deploy.DeploySurveillant;
import com.agun.flyJenkins.schedule.PeriodWork;
import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.service.ServerMeta;
import com.agun.flyJenkins.service.ServiceGroup;

import hudson.Extension;
import hudson.model.Action;

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
		
		System.out.println("=====> is deploy");
		
		Map<String, Object> deployInfoMap = new Hashtable<String, Object>();
		deployInfoMap.put("production", deployMeta.getProduction());
		deployInfoMap.put("serverId", deployMeta.getServerId());
		return deployInfoMap;
	}

	
	public void deployComplete(int serverId){
		
		NetworkSpace networkSpace = NetworkSpace.getInstance();
		Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
		List<DeployLog> deployLogList = FlyFactory.getPeriodWork().getDeployLogList();
		
		for(DeployLog deployLog : deployLogList){
			if(deployLog.isComplete()){
				continue;
			}
			int serverGroupId = deployLog.getGroupId();
			for(List<AgentService> agentList : networkMap.values()){
				System.out.println("=====> loop 1");
				for(AgentService agent : agentList){
					ServiceGroup serviceGroup = agent.getServiceGroup();
					
					if(serviceGroup.getGroupId() == serverGroupId){
						System.out.println("=====> loop 2");
						
						List<ServerMeta> serverMetaList = serviceGroup.getServerMetaList();
						for(ServerMeta serverMeta : serverMetaList){
							if(serverMeta.getServerId() == serverId){
								System.out.println("=====> loop 3");
								synchronized (deployLogList) {
									deployLog.setCompleteCount(deployLog.getCompleteCount() +1);
								}
								if(deployLog.isComplete()){
									System.out.println("=====> loop 4");
									
									FlyFactory.getDeployInfo().setComplete(deployLog.getJobName(), deployLog.getDate());
								}
								
								return;
							}
						}
						break;
					}
				}
			}
		}
	}
	
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("deployInfo".equals(operName)){
			return this.getDeployInfo((String)arg1);
		}else if("deployComplete".equals(operName)){
			this.deployComplete((Integer) arg1);
		}
		return null;
	}
}
