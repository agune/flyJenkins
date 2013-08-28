package com.agun.flyJenkins.persistence;

import java.io.IOException;
import java.util.ArrayList;
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
		
		try {
			serviceGroupSaveable.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		for(ServiceGroup serviceGroup : serviceGroupSaveable.getServiceGroupList()){
			if(serviceGroup.getGroupId() == serviceMeta.getGroupId()){
				List<ServiceMeta> serviceMetaList = serviceGroup.getServiceMetaList();
				if(serviceMetaList == null){
					serviceMetaList = new ArrayList<ServiceMeta>();
				}
				serviceMetaList.add(serviceMeta);
				break;
			}
		}
	}
	
}
