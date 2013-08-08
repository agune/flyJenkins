/**
 * Service 서버의 meta 정보를 관리하는 클래스 
 * @author agun
 */

package com.agun.flyJenkins.service;

import hudson.BulkChange;
import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jenkins.model.Jenkins;


public class ServerMeta implements Saveable{
	/**
	 *  Service Server host
	 */
	private String host;
	/**
	 * deploy destination point
	 */
	private String destination;
	
	/**
	 * deploy type
	 */
	private Integer type;
	
	/**
	 * Service Server Group Id 
	 */
	private Integer groupId;
	
	/**
	 * Service Server Id (auto increase) 
	 */
	private Integer serverId;
	/**
	 * test cmd after deploy 
	 */
	private String testCmd;
	
	/**
	 * priority of deployment  
	 */
	private Integer weight;
	/**
	 * process id of service app
	 */
	private Integer pid;
	
	/**
	 * process id of service name
	 */
	private String name;
	
	
	private String testUrl;

	private List<ServerMeta> serverMetaList;
	
	public String getTestUrl() {
		return testUrl;
	}
	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}
	
	public String getHost() {
		return host;
	}
	public void setServerMetaList(List<ServerMeta> serverMetaList) {
		serverMetaList = serverMetaList;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public String getTestCmd() {
		return testCmd;
	}
	public void setTestCmd(String testCmd) {
		this.testCmd = testCmd;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ServerMeta> getServerMetaList(){
		return this.serverMetaList;
	}
	
	public Map<String, Object> convertMap(){
		Map<String, Object> dataMap = new Hashtable<String, Object>();
		dataMap.put("host", this.getHost());
		dataMap.put("destination", this.getDestination());
		dataMap.put("groupId", this.getGroupId());
		dataMap.put("serverId", this.getServerId());
		dataMap.put("testCmd", this.getTestCmd());
		dataMap.put("type", this.getType());
		dataMap.put("weight", this.getWeight());
		return dataMap;
	}
	
	
	public ServerMeta getCopy(){
		ServerMeta serverMeta =  new ServerMeta();
		serverMeta.setHost(this.host);
		serverMeta.setDestination(this.destination);
		serverMeta.setGroupId(this.groupId);
		serverMeta.setName(this.name);
		serverMeta.setServerId(this.serverId);
		serverMeta.setTestCmd(this.testCmd);
		serverMeta.setTestUrl(this.testUrl);
		serverMeta.setType(this.type);
		serverMeta.setWeight(this.weight);
		serverMeta.setPid(this.pid);
		return serverMeta;
	}
	
	public synchronized void save() {
		
		ServerMeta serverMeta =  this.getCopy();
		
		load();
		
		if(this.serverMetaList != null){
			serverMetaList.add(serverMeta);
		}else{
			serverMetaList = new ArrayList<ServerMeta>();
			serverMetaList.add(serverMeta);
		}
			
		
		if(BulkChange.contains(this))   return;
        try {
            getConfigFile().write(this);
            SaveableListener.fireOnChange(serverMeta, getConfigFile());
        } catch (IOException e) {
           	e.printStackTrace();
        }
    }
	
	protected XmlFile getConfigFile() {
	    return new XmlFile(new File(Jenkins.getInstance().getRootDir(), this.getClass().getSimpleName()+".xml"));
    }
	
	 public synchronized void load() {
	        XmlFile file = getConfigFile();
	        if(!file.exists())
	            return;

	        try {
	            file.unmarshal(this);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
}
