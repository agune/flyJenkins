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
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

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
	private int type;
	
	/**
	 * Service Server Group Id 
	 */
	private int groupId;
	
	/**
	 * Service Server Id (auto increase) 
	 */
	private int serverId;
	/**
	 * test cmd after deploy 
	 */
	private String testCmd;
	
	/**
	 * priority of deployment  
	 */
	private int weight;
	/**
	 * process id of service app
	 */
	private int pid;
	
	/**
	 * process id of service name
	 */
	private String name;
	
	
	public ServerMeta(){
		load();
	}
	public String getHost() {
		return host;
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
	public void setType(int type) {
		this.type = type;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getTestCmd() {
		return testCmd;
	}
	public void setTestCmd(String testCmd) {
		this.testCmd = testCmd;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public synchronized void save() {
        if(BulkChange.contains(this))   return;
        try {
            getConfigFile().write(this);
            SaveableListener.fireOnChange(this, getConfigFile());
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
