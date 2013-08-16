package com.agun.flyJenkins.util;


import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.kohsuke.stapler.bind.JavaScriptMethod;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.deploy.DeployRequest;
import com.agun.flyJenkins.request.ProcessKill;
import com.agun.flyJenkins.request.RequestMap;
import com.agun.flyJenkins.request.RequestQueue;
import com.agun.flyJenkins.request.Requester;
import com.agun.flyJenkins.schedule.PeriodWork;

/**
 * flyJenkins 에서 request 요청한 것을 실제로 처리하는 requester 에게 전달한다. 
 * @author agun
 *
 */
public class AjaxProxy {

	private static AjaxProxy ajaxProxy = new AjaxProxy();
	private Map<String, Requester> commandMap =  new Hashtable<String, Requester>();
	
	private AjaxProxy(){
		ProcessKill processKill = new ProcessKill();
		commandMap.put("processKill", processKill);
	}
	
	public static AjaxProxy getInstance(){
		return ajaxProxy;
	}
	
	@JavaScriptMethod
	public String deployRequest(String type, String jobName, long timeValue ){

		DeployRequest deployRequest = new DeployRequest();
		deployRequest.load();
		List<DeployRequest> deployRequestList = deployRequest.getDeployRequestList();
		
		if(deployRequestList ==  null)
			return "Not exist request";
		
		DeployRequest deployRequestSel = null;
		for(DeployRequest saveDeployRequest : deployRequestList){
			if(saveDeployRequest.getDate().getTime() == timeValue 
					&& saveDeployRequest.getJobName().equals(jobName)){
				deployRequestSel = saveDeployRequest;
				deployRequestSel.setConfirm(true);
				break;
			}
		}
		
		if(deployRequestSel !=null ){
			try {
				
				deployRequestSel.setDeployRequestList(deployRequestList);
				deployRequestSel.edit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "ok";
		}
		return "Not exist request";
	}
	
	@JavaScriptMethod
	public String request(String type, String host, Integer arg){
		
		if(commandMap.containsKey(type) == false)
			return "fail";
		
		Requester requester = commandMap.get(type);
		requester.request(host, arg);
		
		PeriodWork periodWork = FlyFactory.getPeriodWork();
		
		RequestQueue requestQueue = periodWork.getRequestQueue();
		LinkedList<RequestMap>queue = requestQueue.getRequest(host);
		return "ok";
	}
}
