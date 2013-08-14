package com.agun.flyJenkins.util;

import hudson.ExtensionList;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.bind.JavaScriptMethod;

import com.agun.flyJenkins.FlyFactory;
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
