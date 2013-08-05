/**
 * agent instance 를 관리 하는 모델 class
 * @author agun
 */
package com.agun.flyJenkins.service;

public class AgentService {
	private int agentId;
	private String host;
	private ServiceGroup serviceGroup;

	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public ServiceGroup getServiceGroup() {
		return serviceGroup;
	}
	public void setServiceGroup(ServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
	}
	
}
