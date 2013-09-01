package com.agun.flyJenkins.model.util;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import com.agun.flyJenkins.model.ServiceMeta;


/**
 * this class converter of model 
 * @author agun
 *
 */

public class ConvertUtil {

	// convert from ServiceMeta to Map 
	public static Map<String, Object> getMapFromServiceMeta(ServiceMeta  serviceMeta){
		// null value safely
		if(serviceMeta == null)
			return Collections.EMPTY_MAP;
		
		Map<String, Object> resultMap = new Hashtable<String, Object>();
		resultMap.put("host", serviceMeta.getHost());
		resultMap.put("serviceId", serviceMeta.getServiceId());
		resultMap.put("destination", serviceMeta.getDestination());
		resultMap.put("groupId", serviceMeta.getGroupId());
		resultMap.put("command", serviceMeta.getCommand());
		resultMap.put("pid", serviceMeta.getPid());
		resultMap.put("testUrl", serviceMeta.getTestUrl());
		resultMap.put("type", serviceMeta.getType());
		resultMap.put("weight", serviceMeta.getWeight());
		return resultMap;
	}
}
