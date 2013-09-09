package com.agun.flyJenkins.model;

import java.util.Date;

public class DeployLog {

	private String deployId;
	private String jobName;
	private String production;
	private String host;
	private int serviceGroupId;
	private int serviceId;
	private Date date;
	private Date reserveDate;
	private int requestOrder = 0;
	
	public int getRequestOrder() {
		return requestOrder;
	}

	public void setRequestOrder(int requestOrder) {
		this.requestOrder = requestOrder;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}
	
	public int getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(int serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
}
