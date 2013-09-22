package com.agun.flyJenkins.job;

import hudson.model.Action;

public class JobAction implements Action {
	private String jobName;
	
	public JobAction(String jobName){
		this.jobName = jobName;
	}
	
	public String getDisplayName() {
		return "배포요청항목";
	}

	public String getIconFileName() {
		return "notepad.png";
	}

	public String getUrlName() {
		return "/flyJenkins/DeployInfo?jobName=" + jobName;
	}

}
