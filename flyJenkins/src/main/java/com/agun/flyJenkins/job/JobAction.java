package com.agun.flyJenkins.job;

import hudson.model.Action;

public class JobAction implements Action {

	public String getDisplayName() {
		return "deployRequest";
	}

	public String getIconFileName() {
		return "notepad.png";
	}

	public String getUrlName() {
		return "flyRequest";
	}

}
