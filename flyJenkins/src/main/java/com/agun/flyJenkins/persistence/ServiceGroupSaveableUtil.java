package com.agun.flyJenkins.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;

/**
 * this class is Service Group unit persistence helper
 * @author agun
 *
 */
public class ServiceGroupSaveableUtil {

	public static void saveServiceMeta(ServiceMeta serviceMeta){
		ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
		serviceGroupSaveable.load();
		saveServiceMeta(serviceGroupSaveable, serviceMeta);
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
			}
			if(serviceMetaList !=null){
				for(ServiceMeta partServiceMeta : serviceMetaList){
					System.out.println("====> test : " + partServiceMeta);
					
					if(maxServiceId < partServiceMeta.getServiceId())
						maxServiceId = partServiceMeta.getServiceId();
				}
			}
		}
		maxServiceId++;
		serviceMeta.setServiceId(maxServiceId);
		
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
}
