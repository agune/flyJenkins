package com.agun.flyJenkins.model;

import java.util.List;

/**
 * this class is handle service group data
 * @author agun
 *
 */
public class ServiceGroup {
	/**
	 * Service 의 Group Id
	 */
	private int groupId;
	
	/**
	 * Service Group Name;
	 */
	private String groupName;
	
	
	private List<ServiceMeta> serviceMetaList = null;
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<ServiceMeta> getServiceMetaList() {
		return serviceMetaList;
	}

	public void setServiceMetaList(List<ServiceMeta> serviceMetaList) {
		this.serviceMetaList = serviceMetaList;
	}
	
	
}
