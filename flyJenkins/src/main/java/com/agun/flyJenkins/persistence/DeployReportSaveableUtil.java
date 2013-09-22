package com.agun.flyJenkins.persistence;

import java.io.IOException;
import java.util.List;

import com.agun.flyJenkins.model.DeployReport;

public class DeployReportSaveableUtil {

	public static void plusSuccessCount(String deployId){
		DeployReportSaveable deployReportSaveable = new DeployReportSaveable();
		deployReportSaveable.load();
		
		List<DeployReport> deployReportList = deployReportSaveable.getDeployReportList();
		if(deployReportList == null)
			return;
		boolean isDeployReport = false;
		for(DeployReport deployReport :  deployReportList){
			if(deployReport.getDeployId().equals(deployId)){
				deployReport.plusSuccessCount();
				isDeployReport = true;
				break;
			}
		}
		if(isDeployReport){
			deployReportSaveable.setDeployReportList(deployReportList);
			try {
				deployReportSaveable.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void plusfailCount(String deployId){
		DeployReportSaveable deployReportSaveable = new DeployReportSaveable();
		deployReportSaveable.load();
		
		List<DeployReport> deployReportList = deployReportSaveable.getDeployReportList();
		if(deployReportList == null)
			return;
		boolean isDeployReport = false;
		for(DeployReport deployReport :  deployReportList){
			if(deployReport.getDeployId().equals(deployId)){
				deployReport.plusFailCount();
				isDeployReport = true;
				break;
			}
		}
		if(isDeployReport){
			deployReportSaveable.setDeployReportList(deployReportList);
		
			try {
				deployReportSaveable.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
