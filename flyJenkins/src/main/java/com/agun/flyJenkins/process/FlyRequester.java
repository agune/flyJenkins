package com.agun.flyJenkins.process;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.request.RequestMap;
import com.agun.flyJenkins.request.RequestQueue;
import com.agun.flyJenkins.schedule.PeriodWork;
public class FlyRequester implements FlyProcess {
	private static FlyRequester flyRequester = new FlyRequester();
	
	private FlyRequester(){
		
	}
	
	public static FlyRequester getInstance(){
		return FlyRequester.flyRequester;
	}
	
	private Map<String, Object> peekRequest(String host){
		
		PeriodWork periodWork = FlyFactory.getPeriodWork();
		RequestQueue requestQueue = periodWork.getRequestQueue();
		LinkedList<RequestMap> requestLinkedList = requestQueue.getRequest(host);
		if(requestLinkedList == null || requestLinkedList.size() ==0)
			return Collections.EMPTY_MAP;
		
		RequestMap requestMap = requestLinkedList.remove();
		int type = requestMap.getType();
		Object arg = requestMap.getArg();
		Map<String, Object> resultMap= new Hashtable<String, Object>();
		resultMap.put("type", type);
		resultMap.put("arg", arg);
		return resultMap;
	}
	
	public Map<String, Object> run(Object arg1, String operName) {
		if("peekRequest".equals(operName)){
			return peekRequest((String)arg1);
		}
		return null;
	}

}
