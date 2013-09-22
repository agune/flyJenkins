package com.agun.flyJenkins.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;
import com.agun.flyJenkins.model.util.ModelSoting;

/**
 * this class is Service Group unit persistence helper
 * @author agun
 *
 */
public class ServiceGroupSaveableUtil {

	public static ServiceGroup getServiceGroup(int groupId){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
		
		if(serviceGroupList == null)
			return null;
		
		for(ServiceGroup serviceGroup : serviceGroupList){
			if(serviceGroup.getGroupId() == groupId){
				return serviceGroup;
			}
		}
		return null;
	}
	
	
	public static void saveServiceMeta(ServiceMeta serviceMeta){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		saveServiceMeta(serviceGroupSaveable, serviceMeta);
	}
	
	public static void delServiceMeta(ServiceMeta serviceMeta){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = ServiceGroupSaveableUtil.getServiceGroupList(serviceGroupSaveable);
		boolean isDel = false;
		for(ServiceGroup serviceGroup : serviceGroupList){
			List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
			
			for(ServiceMeta saveServiceMeta : serviceMetaList){

				if(serviceMeta.getServiceId().equals(saveServiceMeta.getServiceId())){
					serviceMetaList.remove(saveServiceMeta);
					isDel = true;
					break;
				}
			}
			if(isDel){
				serviceGroup.setServiceMetaList(serviceMetaList);
			}
		}
		if(isDel){
			serviceGroupSaveable.setServiceGroupList(serviceGroupList);
			
			try {
				serviceGroupSaveable.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void delServiceGroup(int groupId){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		List<ServiceGroup> serviceGroupList = ServiceGroupSaveableUtil.getServiceGroupList(serviceGroupSaveable);

		boolean isDel = false;
		for(ServiceGroup serviceGroup : serviceGroupList){
			if(serviceGroup.getGroupId() == groupId){
				serviceGroupList.remove(serviceGroup);
				isDel = true;
				break;
			}
		}
		if(isDel){
			serviceGroupSaveable.setServiceGroupList(serviceGroupList);
			try {
				serviceGroupSaveable.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * save service meta info
	 * @param serviceGroupSaveable
	 * @param serviceMeta
	 */
	public static void saveServiceMeta(ServiceGroupSaveable serviceGroupSaveable, ServiceMeta serviceMeta){
		if(serviceGroupSaveable == null || serviceGroupSaveable.getServiceGroupList() == null)
			return;
		
		if(serviceMeta.getGroupId() == 0)
			return;
		int maxServiceId = 0;
		for(ServiceGroup serviceGroup : serviceGroupSaveable.getServiceGroupList()){
			List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
			if(serviceGroup.getGroupId() == serviceMeta.getGroupId()){
				if(serviceMetaList == null){
					serviceMetaList = new ArrayList<ServiceMeta>();
					serviceGroup.setServiceMetaList(serviceMetaList);
				}
				serviceMetaList.add(serviceMeta);
				ModelSoting.serviceSortByWeight(serviceMetaList);
			}
			if(serviceMetaList !=null && serviceMeta.getServiceId() == 0){
				for(ServiceMeta partServiceMeta : serviceMetaList){
					if(maxServiceId < partServiceMeta.getServiceId())
						maxServiceId = partServiceMeta.getServiceId();
				}
			}
		}
		if(serviceMeta.getServiceId() == 0){
			maxServiceId++;
			serviceMeta.setServiceId(maxServiceId);
		}
		
		try {
			serviceGroupSaveable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<ServiceMeta> getServiceMetaList(){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		return getServiceMetaList(serviceGroupSaveable);
	}
	
	public static List<ServiceMeta> getServiceMetaList(ServiceGroupSaveable serviceGroupSaveable){
		if(serviceGroupSaveable == null || serviceGroupSaveable.getServiceGroupList() == null)
			return Collections.EMPTY_LIST;
		
		List<ServiceMeta> resultServiceMetaList = new ArrayList<ServiceMeta>();
		
		for(ServiceGroup serviceGroup : serviceGroupSaveable.getServiceGroupList()){
			List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
			if(serviceMetaList != null){
				resultServiceMetaList.addAll(serviceMetaList);
			}
		}
		return resultServiceMetaList;
	}
	
	public static List<ServiceGroup> getServiceGroupList(){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		return getServiceGroupList(serviceGroupSaveable);
	}
	
	public static List<ServiceGroup> getServiceGroupList(ServiceGroupSaveable serviceGroupSaveable){
		List<ServiceGroup> serviceGroupList = serviceGroupSaveable.getServiceGroupList();
		if(serviceGroupList == null)
			return Collections.EMPTY_LIST;
		return serviceGroupList;
	}
	
	public static List<ServiceMeta> getServiceMetaListByGroupId(int groupId){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		return ServiceGroupSaveableUtil.getServiceMetaListByGroupId(groupId, serviceGroupSaveable.getServiceGroupList());
	}
	
	public static List<ServiceMeta> getServiceMetaListByGroupId(int groupId, List<ServiceGroup> serviceGroupList){
		if(serviceGroupList == null)
			return Collections.EMPTY_LIST;
		
		for(ServiceGroup serviceGroup : serviceGroupList){
			if(serviceGroup.getGroupId() == groupId)
				return serviceGroup.getServiceMetaList();
		}
		
		return Collections.EMPTY_LIST;
	}	
	
}
