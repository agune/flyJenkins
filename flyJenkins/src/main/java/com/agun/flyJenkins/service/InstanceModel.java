package com.agun.flyJenkins.service;

import java.util.List;

/**
 * service instance model 을 나타내는 class
 * @author agun
 *
 */
public class InstanceModel {
	
	/**
	 * 프로세스 pid
	 */
	private int pid;
	
	/**
	 * serverId
	 */
	private String host;
	/**
	 * 프로세스 인자
	 */
	private String arg;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getArg() {
		return arg;
	}
	public void setArgList(String arg) {
		this.arg = arg;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
}
