package com.agun.agent.function;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.agun.agent.model.AgentMeta;
import com.agun.flyJenkins.util.FlyJenkinsInfoManager;
import com.agun.jenkins.FilePathHelper;
import com.agun.system.AgentInfoManager;

public class CommonFunction {
	static Logger log = Logger.getLogger(CommonFunction.class.getName());
	
	public static void deployComplete(AgentMeta agentMeta, FilePathHelper filePathHelper){
		String lastDeployPath = AgentInfoManager.checkProductionLastDeployDir(agentMeta.getServiceId());
		if(lastDeployPath != null){
			lastDeployPath = lastDeployPath + "/lastProduct.zip";
			FilePath lastProductionPath = new FilePath(new File(lastDeployPath));
			FilePath destinationPath =  new FilePath(new File(agentMeta.getDestination()));
			try {
				destinationPath.zip(lastProductionPath);
				log.debug("zip production : " + agentMeta.getDestination() + " zip to " + lastDeployPath);
				String target = FlyJenkinsInfoManager.getLastBuldInfo(agentMeta.getServiceId());
				filePathHelper.sendCopyTo(lastProductionPath, target);
				log.debug("send production : " + lastDeployPath + " to " + target);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void copyService(int serviceType, String production, String destination, FilePathHelper filePathHelper){
		String target = AgentInfoManager.checkAgentWorkingDir() + "/service";
		FilePath workFilePath = filePathHelper.unzip(production, target);
		FilePath destinationFilePath  = new FilePath(new File(destination));
		
		try {
			if(destinationFilePath.exists() == false)
				destinationFilePath.mkdirs();
			
			List<FilePath> filePathList = workFilePath.listDirectories();
			for(FilePath subFilePath : filePathList){
				subFilePath.moveAllChildrenTo(destinationFilePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
