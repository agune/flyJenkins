package com.agun.flyJenkins.request;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public class RequestQueue {

	private Map<String, LinkedList<RequestMap>> requestQueueMap = new Hashtable<String, LinkedList<RequestMap>>();

	public LinkedList<RequestMap> getRequest(String host){
		if(requestQueueMap.containsKey(host)){
			return requestQueueMap.get(host);
		}
		return null;
	}
	
	
	public void add(String host, RequestMap requestMap){
		if(requestQueueMap.containsKey(host)){
			requestQueueMap.get(host).add(requestMap);
		}else{
			LinkedList<RequestMap> requestQueue = new LinkedList<RequestMap>();
			requestQueue.add(requestMap);
			requestQueueMap.put(host, requestQueue);
		}
	}
	
}
