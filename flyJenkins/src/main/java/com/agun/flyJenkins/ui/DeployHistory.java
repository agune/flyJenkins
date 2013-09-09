package com.agun.flyJenkins.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import hudson.Extension;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.persistence.DeployLogSaveable;
@Extension
public class DeployHistory extends FlyUI {

	@Override
	public String getDescription() {
		return "you can see deploy history info";
	}

	@Override
 	public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
	
	public List<DeployLog> getDeployLogList(){
		DeployLogSaveable deployLogSaveable = new DeployLogSaveable();
		deployLogSaveable.load();
		return deployLogSaveable.getDeployLogList();
	}
	
	
	public Collection<DeployReport> getDeployReportList(){
		Map<String, DeployReport> deployReportMap = FlyFactory.getPeriodWork().getDeployReportMap();
		System.out.println("===> deployReportMap : " + deployReportMap);
		return deployReportMap.values();
	}
	
	@Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
	
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
