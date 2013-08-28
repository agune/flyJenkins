package com.agun.flyJenkins.persistence;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jenkins.model.Jenkins;

import org.junit.Test;

import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.model.ServiceMeta;

public class ServiceGroupSaveableTest {

	@Test
	public void saveTest() {
		
		/**
		 * create test service group data
		 */
		ServiceGroup serviceGroup = createFacadeServiceGroup();
		List<ServiceGroup> serviceGroupList = new ArrayList<ServiceGroup>();
		serviceGroupList.add(serviceGroup);
	
		
		try {
			ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
			serviceGroupSaveable.setSaveDir("/Users/pdc222/test/");
			serviceGroupSaveable.setServiceGroupList(serviceGroupList);
			serviceGroupSaveable.save();
		} catch (IOException e) {
			assertTrue("not saved service group data: " + e.getMessage(), false);
		}
		
		ServiceGroupSaveable readServiceGroupSaveable = new ServiceGroupSaveable();
		readServiceGroupSaveable.setSaveDir("/Users/pdc222/test/");
		readServiceGroupSaveable.load();
		
		List<ServiceGroup> readServiceGroupList = readServiceGroupSaveable.getServiceGroupList();
	
		assertTrue("not save size", readServiceGroupList.size() == 1);
		int serverIndex = 1;
		for(ServiceGroup indexServiceGroup : readServiceGroupList){
			List<ServiceMeta> serviceMetaList = indexServiceGroup.getServiceMetaList();
			assertTrue("not service size: " + serverIndex,  serviceMetaList.size() == 10);
			serverIndex = 1;
			for(ServiceMeta serviceMeta : serviceMetaList){
				assertTrue("not service Id: " + serverIndex,  serviceMeta.getServiceId() == serverIndex);
				serverIndex++;
			}
		}
	}
	
	
	private ServiceGroup createFacadeServiceGroup(){
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupId(1);
		serviceGroup.setGroupName("A Service");
			
		List<ServiceMeta> serviceMetaList = new ArrayList<ServiceMeta>();
		
		for(int i=1; i<11; i++){
			ServiceMeta serviceMeta = new ServiceMeta();
			serviceMeta.setServiceId(i);
			serviceMetaList.add(serviceMeta);
		}
		serviceGroup.setServiceMetaList(serviceMetaList);
		return serviceGroup;
	}

}
