package com.agun.flyJenkins.model;

import java.util.List;

/**
 * this class is flyAgent Service
 * agent has service meta info
 * @author agun
 * 
 */
public class AgentService {
	/**
	 * agent id
	 */
	private int agentId;
	
	/**
	 * host of agent  
	 */
	private String host;
	
	/**
	 * state of agent 
	 */
	private boolean isRun = false;
	
	
	/***
	 * service meta info List
	 */
	private List<ServiceMeta> serviceMetaList;
	
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

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public List<ServiceMeta> getServiceMetaList() {
		return serviceMetaList;
	}

	public void setServiceMetaList(List<ServiceMeta> serviceMetaList) {
		this.serviceMetaList = serviceMetaList;
	}
}
