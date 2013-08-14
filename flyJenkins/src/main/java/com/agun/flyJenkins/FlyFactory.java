package com.agun.flyJenkins;

import hudson.ExtensionList;
import jenkins.model.Jenkins;

import com.agun.flyJenkins.schedule.PeriodWork;

public class FlyFactory {

	public static PeriodWork getPeriodWork(){
		Jenkins jenkins = Jenkins.getInstance();
		ExtensionList<PeriodWork> extensionList  = jenkins.getExtensionList(PeriodWork.class);
		PeriodWork periodWork = extensionList.get(PeriodWork.class);
		return periodWork;
	}
}
