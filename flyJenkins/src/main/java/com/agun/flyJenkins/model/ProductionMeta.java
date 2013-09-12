package com.agun.flyJenkins.model;

import java.util.Date;

public class ProductionMeta {
	
	private String jobName;
	private Date createDate;
	private String productionPath;
	private String productionPathOfJob;	
	private int serviceGroup;
	private int buildNumber;
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public int getBuildNumber() {
		return buildNumber;
	}
	
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public int getServiceGroup() {
		return serviceGroup;
	}
	
	public void setServiceGroup(int serviceGroup) {
		this.serviceGroup = serviceGroup;
	}
	
	public String getProductionPath() {
		return productionPath;
	}
	
	public void setProductionPath(String productionPath) {
		this.productionPath = productionPath;
	}

	public String getProductionPathOfJob() {
		return productionPathOfJob;
	}

	public void setProductionPathOfJob(String productionPathOfJob) {
		this.productionPathOfJob = productionPathOfJob;
	}
}
