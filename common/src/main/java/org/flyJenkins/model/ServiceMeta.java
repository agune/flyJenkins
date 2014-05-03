/**
 * Service info Model
 * @author agun
 */

package org.flyJenkins.model;

import java.util.Date;

public class ServiceMeta {
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
	private int groupID;
	
	/**
	 * Service Server Id (auto increase) 
	 */
	private int serviceID;
	/**
	 * test cmd after deploy 
	 */
	private String command;
	
	/**
	 * priority of deployment  
	 */
	private int weight;
	
		
	/**
	 * test url info
	 */
	private String testUrl;
	
	private Date createDate;
	
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


	public int getGroupID() {
		return groupID;
	}


	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}


	public int getServiceID() {
		return serviceID;
	}


	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getTestUrl() {
		return testUrl;
	}


	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}


	public long getCreateDate() {
		if(createDate == null) return 0L;
		return createDate.getTime();
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
