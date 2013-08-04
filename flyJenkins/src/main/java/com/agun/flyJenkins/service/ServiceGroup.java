/**
 * 배포하는 서비스 그룹을 관리 하는 class
 * @author agun
 */
package com.agun.flyJenkins.service;

import java.util.List;

public class ServiceGroup {
	List<ServerMeta> serverMetaList;

	public List<ServerMeta> getServerMetaList() {
		return serverMetaList;
	}

	public void setServerMetaList(List<ServerMeta> serverMetaList) {
		this.serverMetaList = serverMetaList;
	}
}
