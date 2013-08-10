package com.agun.agent.adapter;

import java.io.File;
import java.io.IOException;

import hudson.FilePath;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;

public class TomcatService implements ServiceType {

	FilePathHelper filePathHelper;

	
	public FilePathHelper getFilePathHelper() {
		return filePathHelper;
	}

	public void setFilePathHelper(FilePathHelper filePathHelper) {
		this.filePathHelper = filePathHelper;
	}

	@Override
	public int getPid(AgentMeta agentMeta) {
		int pid = ProcessTreeHelper.getPid(agentMeta.getDestination());
		
		if(pid > 0){
			agentMeta.setPid(pid);
			return pid;
		}
		return 0;
	}

	@Override
	public boolean deploy(AgentMeta agentMeta) {
		return false;
	}

	@Override
	public boolean getProduction(AgentMeta agentMeta, String production) {
		String productionPath = AgentInfoManager.getProductionPath(agentMeta.getId());
		System.out.println("copy : ===> " + production + "--->" + productionPath);
		filePathHelper.copyTo(production, productionPath);
		
		FilePath filePath = new FilePath(new File(productionPath));
		try {
			if(filePath.exists())
				return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
