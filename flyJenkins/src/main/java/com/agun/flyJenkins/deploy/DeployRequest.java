package com.agun.flyJenkins.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jenkins.model.Jenkins;
import hudson.BulkChange;
import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.User;
import hudson.model.listeners.SaveableListener;


/**
 * deploy request 에 대한 모델
 * @author agun
 *
 */

public class DeployRequest{

	/**
	 * 요청자
	 */
	private String requester;
	/**
	 * deploy 대상 job
	 */
	private String jobName;
	
	/**
	 * 배포 대상 파일 
	 */
	private String production;
	
	/**
	 * 허가자
	 */
	private String licenser;
	
	/**
	 * request 요청 날짜
	 */
	private Date date;
	
	/**
	 * 배포 완료 여부
	 */
	private boolean run = false;
	
	/**
	 *  배포 승인 여부
	 */
	private boolean confirm = false;
	
	/**
	 * 배포 되어야 할 서비스 그룹
	 */
	private int serverGroup = 0;
	
	
	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getLicenser() {
		return licenser;
	}

	public void setLicenser(String licenser) {
		this.licenser = licenser;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public int getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(int serverGroup) {
		this.serverGroup = serverGroup;
	}
	
	
	
	public boolean isCheckConfirmUser(){
		User user = User.current();
		if(user == null)
			return false;
		
		if(user.getId().equals(this.licenser))
			return true;
		return false;
	}

	
	
}
