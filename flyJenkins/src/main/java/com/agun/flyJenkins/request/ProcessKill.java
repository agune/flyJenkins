package com.agun.flyJenkins.request;

import hudson.ExtensionList;

import java.util.Hashtable;
import java.util.Map;

import jenkins.model.Jenkins;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.schedule.PeriodWork;

/**
 * process 를 kill 하는 request 를 request queue 에 요청 한다.
 * 
 * @author agun
 *
 */

public class ProcessKill implements Requester {
	public Object request(String host, Object arg) {
		int pid = (Integer) arg;
		Map<String, Object> argMap = new Hashtable<String, Object>();
		argMap.put("host", host);
		argMap.put("pid", pid);
		RequestMap requestMap = new RequestMap();
		requestMap.setType(1);
		requestMap.setArg(argMap);
		
		/**
		 * request 를 queue 저장
		 */
		PeriodWork periodWork = FlyFactory.getPeriodWork();
		RequestQueue requestQueue = periodWork.getRequestQueue();
		requestQueue.add(host, requestMap);
		
		return true;
	}

}
