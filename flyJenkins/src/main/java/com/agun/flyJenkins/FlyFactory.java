package com.agun.flyJenkins;

import java.util.List;
import java.util.Map;

import hudson.ExtensionList;
import hudson.model.Action;
import hudson.model.Item;
import hudson.model.TopLevelItem;
import hudson.model.WorkspaceBrowser;
import hudson.model.Job;
import hudson.model.Project;
import jenkins.model.Jenkins;

import com.agun.flyJenkins.model.util.IncreaseIndexer;
import com.agun.flyJenkins.schedule.PeriodWork;
import com.agun.flyJenkins.ui.DeployInfo;

public class FlyFactory {

	public static PeriodWork getPeriodWork(){
		Jenkins jenkins = Jenkins.getInstance();
		ExtensionList<PeriodWork> extensionList  = jenkins.getExtensionList(PeriodWork.class);
		PeriodWork periodWork = extensionList.get(PeriodWork.class);
		return periodWork;
	}
	
	public static DeployInfo getDeployInfo(){
		Jenkins jenkins = Jenkins.getInstance();
		ExtensionList<DeployInfo> extensionList  = jenkins.getExtensionList(DeployInfo.class);
		DeployInfo deployInfo = extensionList.get(DeployInfo.class);
		return deployInfo;
	}
	
	public static IncreaseIndexer getIncreaseIndexer(){
		return IncreaseIndexer.getInstance();
	}
	
	public static Map<String, Object> getPropertiesOfJob(String name){
		Jenkins jenkins = Jenkins.getInstance();
	
		List<Item> itemList = jenkins.getAllItems();
	
		for(Item item : itemList){
			for(Job job : item.getAllJobs()){
				if(name.equals(job.getName())){
					return job.getProperties();
				}
			}
		}
		return null;
	}
	
	
	
	public static String getRootPathOfJob(String name){
		Jenkins jenkins = Jenkins.getInstance();
		List<Item> itemList = jenkins.getAllItems();
		for(Item item : itemList){
			for(Job job : item.getAllJobs()){
				if(name.equals(job.getName())){
					return job.getRootDir().getAbsolutePath();
				}
			}
		}
		return null;
	}
}
