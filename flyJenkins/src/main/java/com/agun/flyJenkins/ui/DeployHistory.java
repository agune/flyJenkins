package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import hudson.Extension;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.persistence.DeployLogSaveable;
import com.agun.flyJenkins.persistence.DeployReportSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;
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
	
	
	public	List<DeployReport> getDeployReportList(){
		DeployReportSaveable deployReportSaveable = new DeployReportSaveable();
		deployReportSaveable.load();
		return deployReportSaveable.getDeployReportList();
	}
	
	public String getGroupName(int groupId){
		ServiceGroup serviceGroup = ServiceGroupSaveableUtil.getServiceGroup(groupId);
		if(serviceGroup == null)
			return null;
		return serviceGroup.getGroupName();
	}
	
	
	public boolean isDeployLogComplete(String deployKey , int order, Collection<DeployReport> deployReportList){
		for(DeployReport deployReport : deployReportList){
			if(deployReport.getDeployId().equals(deployKey)){
				if(deployReport.getSuccessCount()  >= order){
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	
	@Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
	
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
