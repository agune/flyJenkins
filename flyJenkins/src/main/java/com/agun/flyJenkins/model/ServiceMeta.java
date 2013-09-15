package com.agun.flyJenkins.model;

/**
 * this class is handle service meta info
 * @author agun
 *
 */
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
	private Integer type;
	
	/**
	 * Service Server Group Id 
	 */
	private Integer groupId;
	
	/**
	 * Service Server Id (auto increase) 
	 */
	private Integer serviceId;
	/**
	 * test cmd after deploy 
	 */
	private String command;
	
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
	
	
	/**
	 * test url info
	 */
	private String testUrl;


	/**
	 * install able
	 */
	private boolean installAble;

	
	private int dependenceService =0 ;
	
	
	

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


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getGroupId() {
		if(groupId == null)
			return 0;
		return groupId;
	}


	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}


	public Integer getServiceId() {
		if(this.serviceId == null)
			return 0;
		
		return this.serviceId;
	}


	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public Integer getWeight() {
		return weight;
	}


	public void setWeight(Integer weight) {
		this.weight = weight;
	}


	public Integer getPid() {
		if(pid == null)
			return 0;
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


	public String getTestUrl() {
		return testUrl;
	}


	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}


	public boolean isInstallAble() {
		return installAble;
	}


	public void setInstallAble(boolean installAble) {
		this.installAble = installAble;
	}
	
	public int getDependenceService() {
		return dependenceService;
	}

	public void setDependenceService(int dependenceService) {
		this.dependenceService = dependenceService;
	}

}
