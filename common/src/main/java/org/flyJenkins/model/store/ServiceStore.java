package org.flyJenkins.model.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.flyJenkins.model.ServiceGroup;
import org.flyJenkins.model.ServiceMeta;

public class ServiceStore {
	
	Map<Integer, ServiceMeta> serviceMap;
	Map<Integer, ServiceGroup> groupMap;
	
	
	public ServiceStore(){
		serviceMap = new ConcurrentHashMap<Integer, ServiceMeta>();
		groupMap = new ConcurrentHashMap<Integer, ServiceGroup>();
	}
	
	private boolean vaildService(ServiceMeta serviceMeta){
		
		if(serviceMeta == null) return false;
		
		if(serviceMeta.getServiceID() < 1)
			return false;
		if(serviceMeta.getGroupID() < 1)
			return false;
		
		if(serviceMeta.getHost() == null || serviceMeta.getHost().length() < 1)
			return false;		
		return true;
	}
	
	public void storeService(ServiceMeta serviceMeta){
		if(vaildService(serviceMeta) == false) return;
		
		// if isn's group, it go wrong
		if(!isGroup(serviceMeta.getGroupID())) return;
		
		// if exist service, remove service.
		if(isService(serviceMeta.getServiceID())) removeService(serviceMeta);
		
		ServiceGroup oldServiceGroup = groupMap.get(serviceMeta.getGroupID());
		
		List<ServiceMeta> serviceList = oldServiceGroup.getServiceMetaList();
		
		if(serviceList == null){
			serviceList = new ArrayList<ServiceMeta>();
			oldServiceGroup.setServiceMetaList(serviceList);
		}
		
		for(ServiceMeta iServiceMeta : serviceList){
			if(iServiceMeta.getServiceID() == serviceMeta.getServiceID()){
				serviceList.remove(iServiceMeta);
				break;
			}
		}
		serviceList.add(serviceMeta);
		serviceMap.put(serviceMeta.getServiceID(), serviceMeta);
	}
	
	public void storeGroup(ServiceGroup serviceGroup){
		if(serviceGroup == null || serviceGroup.getName() == null || serviceGroup.getName().length() == 0)
			return;
			
		// if exist group, just edit name.
		if(isGroup(serviceGroup.getGroupID())){
			ServiceGroup oldServiceGroup = groupMap.get(serviceGroup.getGroupID());
			oldServiceGroup.setName(serviceGroup.getName());
			return;
		}
		groupMap.put(serviceGroup.getGroupID(), serviceGroup);
	}
	
	public List<ServiceMeta> getServiceList(){
		return (List<ServiceMeta>) serviceMap.values();
	}
	
	public List<ServiceGroup> getGroupList(){
		return (List<ServiceGroup>) groupMap.values();
	}
	
	public ServiceGroup getGroupByID(int ID){
		return groupMap.get(ID);
	}	
	public ServiceMeta getServiceByID(int ID){
		return serviceMap.get(ID);
	}
	
	public List<ServiceMeta> getServiceListByGroupID(int ID){
		if(isGroup(ID) == false) return Collections.EMPTY_LIST;
		ServiceGroup serviceGroup = groupMap.get(ID);
		List<ServiceMeta> serviceList = serviceGroup.getServiceMetaList();
		if(serviceList == null) return Collections.EMPTY_LIST;
		return serviceList;
	}
	
	
	public void removeService(ServiceMeta serviceMeta){
		ServiceMeta oldServiceMeta = serviceMap.get(serviceMeta.getServiceID());
		removeServiceInGroup(oldServiceMeta.getGroupID(), oldServiceMeta.getServiceID());
		serviceMap.remove(serviceMeta.getServiceID());
	}
	
	public void removeServiceInGroup(int groupID, int serviceID){
		if(isGroup(groupID) == false) return;
		ServiceGroup serviceGroup = groupMap.get(groupID);
		
		List<ServiceMeta> serviceList = serviceGroup.getServiceMetaList();
		if(serviceList == null) return;
		
		for(ServiceMeta serviceMeta : serviceList){
			if(serviceMeta.getServiceID() == serviceID){
				serviceList.remove(serviceMeta);
				break;
			}
		}
	}
	
	public boolean isService(int serviceID){
		return serviceMap.containsKey(serviceID);
	}
	
	public boolean isGroup(int groupID){
		return groupMap.containsKey(groupID);
	}
}
