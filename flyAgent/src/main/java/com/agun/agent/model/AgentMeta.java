package com.agun.agent.model;

public class AgentMeta {

	private int id;
	private int type;
	private int pid;
	private String name;
	private String testCmd;
	private String destination;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getTestCmd() {
		return testCmd;
	}
	public void setTestCmd(String testCmd) {
		this.testCmd = testCmd;
	}

}
