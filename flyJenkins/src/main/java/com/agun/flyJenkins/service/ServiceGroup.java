/**
 * 배포하는 서비스 그룹을 관리 하는 class
 * @author agun
 */
package com.agun.flyJenkins.service;

import java.util.List;

public class ServiceGroup {
	/**
	 * Service 의 Group Id
	 */
	private int groupId;

	/**
	 * Service Group 에 포함된 서비스 Meta 정보 리스
	 */
	private List<ServerMeta> serverMetaList;
	
	
	public List<ServerMeta> getServerMetaList() {
		return serverMetaList;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setServerMetaList(List<ServerMeta> serverMetaList) {
		this.serverMetaList = serverMetaList;
	}
}
