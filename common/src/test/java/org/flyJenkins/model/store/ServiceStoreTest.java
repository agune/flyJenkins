package org.flyJenkins.model.store;

import static org.junit.Assert.*;

import java.util.List;

import org.flyJenkins.model.ServiceGroup;
import org.flyJenkins.model.ServiceMeta;
import org.junit.Test;

public class ServiceStoreTest {

	@Test
	public void storeTest() {
		ServiceStore serviceStore = new ServiceStore();
		
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupID(1);
		serviceGroup.setName("group1");

		ServiceGroup serviceGroup2 = new ServiceGroup();
		serviceGroup2.setGroupID(2);
		serviceGroup2.setName("group2");

		
		serviceStore.storeGroup(serviceGroup);
		serviceStore.storeGroup(serviceGroup2);
		
		ServiceGroup groupResult = serviceStore.getGroupByID(1);
		assertTrue("not match group id", groupResult.getGroupID() == 1);
		assertTrue("not match group name", groupResult.getName().equals("group1") );
		
		
		groupResult = serviceStore.getGroupByID(2);
		assertTrue("not match group id2", groupResult.getGroupID() == 2);
		assertTrue("not match group name2", groupResult.getName().equals("group2") );

		// service assert start
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setServiceID(1);
		serviceMeta.setGroupID(1);
		serviceMeta.setHost("127.0.0.1");
		serviceStore.storeService(serviceMeta);
		
		ServiceMeta serviceMeta2 = new ServiceMeta();
		serviceMeta2.setServiceID(2);
		serviceMeta2.setGroupID(1);
		serviceMeta2.setHost("127.0.0.2");
		serviceStore.storeService(serviceMeta2);
			
		ServiceMeta serviceMeta3 = new ServiceMeta();
		serviceMeta3.setServiceID(3);
		serviceMeta3.setGroupID(2);
		serviceMeta3.setHost("127.0.0.3");
		serviceStore.storeService(serviceMeta3);
		
		ServiceMeta serviceResult = serviceStore.getServiceByID(1); 
		assertTrue("not match service id by service", serviceResult.getServiceID() == 1);
		assertTrue("not match group id by service", serviceResult.getGroupID() == 1);
		assertTrue("not match host by service", serviceResult.getHost().equals("127.0.0.1"));
		
		serviceResult = serviceStore.getServiceByID(2); 
		assertTrue("not match service id by service2", serviceResult.getServiceID() == 2);
		assertTrue("not match group id by service2", serviceResult.getGroupID() == 1);
		assertTrue("not match host by service2", serviceResult.getHost().equals("127.0.0.2"));
		
		List<ServiceMeta> serviceMetaList = serviceStore.getServiceListByGroupID(1);
		assertTrue("not match service count", serviceMetaList.size() == 2);
	
		serviceMetaList = serviceStore.getServiceListByGroupID(2);
		assertTrue("not match service count2", serviceMetaList.size() == 1);
		
		
		ServiceMeta serviceMeta4 = new ServiceMeta();
		serviceMeta4.setServiceID(1);
		serviceMeta4.setGroupID(2);
		serviceMeta4.setHost("127.0.0.3");
		serviceStore.storeService(serviceMeta4);
	
		
		 serviceMetaList = serviceStore.getServiceListByGroupID(1);
		assertTrue("not match service count3", serviceMetaList.size() == 1);
	
		serviceMetaList = serviceStore.getServiceListByGroupID(2);
		assertTrue("not match service count4", serviceMetaList.size() == 2);
	
	
	}

}
