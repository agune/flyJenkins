package com.agun.flyJenkins.deploy;

import hudson.BulkChange;
import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jenkins.model.Jenkins;
/**
 * deploy 이력을 관리하는 모
 * @author agun
 *
 */
public class DeployLog {
	/**
	 * jobName
	 */
	private String jobName;
	private Date date;
	private int stepOrder = 0;
	private int groupId;
	private int workSize = 0;
	private int completeCount = 0;
	
	private String production;
	
	private List<DeployLog> deployLogList;
	
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
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
	public int getStepOrder() {
		return stepOrder;
	}
	public void setStepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getWorkSize() {
		return workSize;
	}
	public void setWorkSize(int workSize) {
		this.workSize = workSize;
	}
	public int getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}
	public boolean isComplete(){
		if(this.stepOrder == 0)
			return false;
		if(this.completeCount > 0 && 
				this.completeCount >= this.workSize)
			return true;
		return false;
	}
	
	public List<DeployLog> getDeployLogList() {
		return deployLogList;
	}
	public void setDeployLogList(List<DeployLog> deployLogList) {
		this.deployLogList = deployLogList;
	}
		
}
