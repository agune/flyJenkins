package com.agun.flyJenkins;

import java.util.List;
import java.util.Map;

import hudson.ExtensionList;
import hudson.model.Item;
import hudson.model.TopLevelItem;
import hudson.model.Job;
import hudson.model.Project;
import jenkins.model.Jenkins;

import com.agun.flyJenkins.schedule.PeriodWork;

public class FlyFactory {

	public static PeriodWork getPeriodWork(){
		Jenkins jenkins = Jenkins.getInstance();
		ExtensionList<PeriodWork> extensionList  = jenkins.getExtensionList(PeriodWork.class);
		PeriodWork periodWork = extensionList.get(PeriodWork.class);
		return periodWork;
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
}
