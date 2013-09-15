package com.agun.flyJenkins.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.agun.flyJenkins.model.DeployMeta;

import jenkins.model.Jenkins;
import hudson.BulkChange;
import hudson.XmlFile;
import hudson.model.Saveable;
import hudson.model.listeners.SaveableListener;

public class DeployMetaSaveable implements Saveable{

	List<DeployMeta> deployMetaList = null;
	
	
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

	public List<DeployMeta> getDeployMetaList() {
		return deployMetaList;
	}

	public void setDeployMetaList(List<DeployMeta> deployMetaList) {
		this.deployMetaList = deployMetaList;
	}

}
