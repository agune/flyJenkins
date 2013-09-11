package com.agun.agent.adapter;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;



public class GeneralService implements ServiceType {

	FilePathHelper filePathHelper;
	
	@Override
	public int getPid(AgentMeta agentMeta) {
		if(agentMeta.getDestination() == null){
			return 0;
		}
		
		int pid = ProcessTreeHelper.getPid(agentMeta.getDestination());
		if(pid > 0){
			agentMeta.setPid(pid);
			return pid;
		}
		return 0;
	}

	@Override
	public boolean deploy(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getProduction(AgentMeta agentMeta, String production) {
		
		createCmdSh(agentMeta.getDestination(), production);
		String filename = extractFilename(production);
		AgentInfoManager.checkProductionDir(agentMeta.getServiceId());
		String productionPath = AgentInfoManager.getProductionPath(agentMeta.getServiceId(), filename);
		FilePath filePath = new FilePath(new File(productionPath));
		
		try {
			filePathHelper.copyTo(production, productionPath);
			if(filePath.exists()){
				System.out.println("======>" + agentMeta.getDestination() + "/" + filename + "," + productionPath);
				FilePath sourceFilePath = new FilePath(new File(agentMeta.getDestination() + "/" + filename));
				filePath.renameTo(sourceFilePath);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean stop(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		return false;
	}

	
	private void createCmdSh(String destination, String production){
		FilePath sourceFilePath = new FilePath(new File(destination + "/cmd.sh"));
		try {
			if(sourceFilePath.exists() == false){
				String content = "#! /bin/bash \n pid=`ps ex | grep "+production+" | grep -v grep | awk '{print $1}'`  \n echo $pid";
				sourceFilePath.write(content, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void complete(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		
	}
	
	
	private String extractFilename(String filepath){
		String filePathNormal = filepath.replaceAll("\\\\", "/");
		String[] fileExtractList = filePathNormal.split("/");
		if(fileExtractList != null && fileExtractList.length > 0){
			return fileExtractList[fileExtractList.length -1];
		}
		return filepath;
	}

	public FilePathHelper getFilePathHelper() {
		return filePathHelper;
	}

	public void setFilePathHelper(FilePathHelper filePathHelper) {
		this.filePathHelper = filePathHelper;
	}
}
