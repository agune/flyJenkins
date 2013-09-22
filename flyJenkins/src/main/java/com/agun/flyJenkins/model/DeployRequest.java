package com.agun.flyJenkins.model;

import hudson.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class DeployRequest {

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
	
	private String displayProduction;
	
	/**
	 * request 요청 날짜
	 */
	private Date date;
	
	/**
	 *  배포 승인 여부
	 */
	private boolean confirm = false;
	
	/**
	 * 배포 되어야 할 서비스 그룹
	 */
	private int serverGroup = 0;
	
	private int buildNumber = 0;
	
	
	

	/**
	 * deploy queue 여부 
	 */
	private boolean isQueue = false; 
	
	/**
	 * 예약 시간 
	 */
	private Date reserveDate;
	
	
	private Map<String, Boolean> licenserMap = new Hashtable<String, Boolean>();
	
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
		
		if(licenser == null || licenser.length() == 0)
			return;
		
		String[] userIds = licenser.split(",");
		for(String userId : userIds){
			licenserMap.put(userId, false);
		}
		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public boolean isQueue() {
		return isQueue;
	}

	public void setQueue(boolean isQueue) {
		this.isQueue = isQueue;
	}
	
	public boolean isCheckConfirmUser(){
		User user = User.current();
		if(user == null)
			return false;
		
		for(boolean isCheck : licenserMap.values()){
			if(isCheck == false)
				return false;
		}
		
		if(user.getId().equals(this.requester))
			return true;
		return false;
	}

	public boolean isCheckUser(){
		User user = User.current();
		if(user == null)
			return false;
		
		if(licenserMap.containsKey(user.getId()) && licenserMap.get(user.getId()) == false){
			return true;
		}
		return false;
	}
	public Date getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getDisplayProduction() {
		return displayProduction;
	}

	public void setDisplayProduction(String displayProduction) {
		this.displayProduction = displayProduction;
	}

	public Map<String, Boolean> getLicenserMap() {
		return licenserMap;
	}
	public List<String> getNotOkUserList(){
		List<String> userIdList = new ArrayList<String>();
		
		for(Entry<String, Boolean> entry : licenserMap.entrySet()){
			if(entry.getValue() == false){
				userIdList.add(entry.getKey());
			}
		}
		return userIdList;
	}
	
	public void okLicenser(String userId){
		if(licenserMap.containsKey(userId) == false){
			return;
		}
		licenserMap.put(userId, true);
	}
}
