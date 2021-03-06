package com.agun.flyJenkins.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jenkins.model.Jenkins;

import com.agun.flyJenkins.model.ServiceGroup;

import hudson.BulkChange;
import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;

/**
 * ServiceGroupSaveable managed persistent ServiceGroup data  
 * @author agun
 *
 */
public class ServiceGroupSaveable implements Saveable {

	private List<ServiceGroup> serviceGroupList=null;
	private String saveDir = null;
	
	
	/**
	 * save data
	 */
	public void save() throws IOException {
		if(BulkChange.contains(this))   return;
        try {
            getConfigFile().write(this);
            SaveableListener.fireOnChange(this, getConfigFile());
        } catch (IOException e) {
           	e.printStackTrace();
        }
		
	}
	
	protected XmlFile getConfigFile() {
		if(saveDir !=null)
		    return new XmlFile(new File(saveDir, this.getClass().getSimpleName()+".xml"));
	    		
	    return new XmlFile(new File(Jenkins.getInstance().getRootDir(), this.getClass().getSimpleName()+".xml"));
    }
	
	/**
	 * open file and load data
	 */
	 public synchronized void load() {
		 XmlFile file = getConfigFile();
		 if(!file.exists())
			 return;
		 try {
			 file.unmarshal(this);
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }

	public List<ServiceGroup> getServiceGroupList() {
		return serviceGroupList;
	}

	public void setServiceGroupList(List<ServiceGroup> serviceGroupList) {
		this.serviceGroupList = serviceGroupList;
	}

	public String getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}
}
