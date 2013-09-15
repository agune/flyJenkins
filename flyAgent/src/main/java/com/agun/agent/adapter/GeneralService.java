package com.agun.agent.adapter;

import hudson.FilePath;
import hudson.Launcher.LocalLauncher;
import hudson.util.StreamTaskListener;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.agun.agent.function.CommonFunction;
import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;



public class GeneralService implements ServiceType {

	static Logger log = Logger.getLogger(GeneralService.class.getName());
	
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
		stop(agentMeta);
		return start(agentMeta);
	}

	@Override
	public boolean getProduction(AgentMeta agentMeta, String production) {
	
		/**
		 * checked production dir
		 */
		AgentInfoManager.checkProductionDir(agentMeta.getServiceId());
		
		/**
		 * obtain filename of production
		 */
		String filename = extractFilename(production);
		
		/**
		 * obtain production path on agent
		 */
		String productionPath = AgentInfoManager.getProductionPath(agentMeta.getServiceId(), filename);
		
		FilePath filePath = new FilePath(new File(productionPath));
		try {
			// copy to production 
			filePathHelper.copyTo(production, productionPath);
			if(filePath.exists()){
				/**
				 * production move agent destination => service destination
				 */
				System.out.println("======>" + agentMeta.getDestination() + "/" + filename + "," + productionPath);
				FilePath sourceFilePath = new FilePath(new File(agentMeta.getDestination() + "/" + filename));
				filePath.renameTo(sourceFilePath);
				
				sourceFilePath.chmod(0755);
				
				/**
				 * make start script
				 */
				createStartSh(agentMeta.getDestination(), filename);
			
				/**
				 * make stop script
				 */
				createStopSh(agentMeta.getDestination(), filename);
			
				/**
				 * make command script
				 */
				createCommandSh(agentMeta.getDestination(), agentMeta.getCommand());
				
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
		boolean isOk  = false;
		if(agentMeta.getPid() > 0){
			isOk =  runCommand(agentMeta.getDestination() + "/stop.sh");
		}
		return isOk;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		System.out.println("====> start : process: " + agentMeta.getDestination() + "/start.sh" );
		
		boolean isOk =  runCommand(agentMeta.getDestination() + "/start.sh");
		isOk = checkPid(false, agentMeta);
		if(	isOk 
				&& (agentMeta.getCommand() !=null) 
				&& (agentMeta.getCommand().length() > 0)){
			isOk = runCommand(agentMeta.getDestination() + "/command.sh");
		}
		return isOk;
	}
	
	@Override
	public boolean monitoring(AgentMeta agentMeta){
		return true;
	}
	
	
	/**
	 * 15초 프로세스의 상태를 확인하고 
	 * @param isDown
	 * @param agentMeta
	 * @return
	 */
	private boolean checkPid(boolean isDown, AgentMeta agentMeta){
		try {
			int count = 0;
			while(true){	
					Thread.sleep(5000);		
					int pid = getPid(agentMeta);
					if(isDown && pid == 0)
						return true;
					else if(isDown == false && pid > 0)
						return true;
				
					count++;
					if(count == 3)
						break;
			}
		} catch (InterruptedException e) {
		}
		return false;
	}
	
	private boolean runCommand(String command){
		LocalLauncher launcher = new LocalLauncher(new StreamTaskListener(System.out, null));
        try {
                launcher.launch().cmds(command).stderr(System.out)
                .stdout(System.out)
                .start();
        } catch (IOException e) {
                e.printStackTrace();
                return false;
        }         
        return true;
	}
	
	
	private void createStartSh(String destination, String production){
		String command = "#! /bin/bash \n " + destination + "/" + production + " &";
		createSh(destination, "start.sh", command);
	}
	
	private void createStopSh(String destination, String production){
		String command = "#! /bin/bash \n pid=`ps ex | grep "+destination+"/"+production+" | grep -v grep | awk '{print $1}'`  \n kill -9 $pid";
		createSh(destination, "stop.sh", command);
	}
	private void createCommandSh(String destination, String command){
		if(command == null || command.length() == 0)
			return;
		createSh(destination, "command.sh", command);
	}
	
	private void createSh(String destination, String scriptName, String command){
		FilePath sourceFilePath = new FilePath(new File(destination + "/" + scriptName));
		try {
			sourceFilePath.write(command, null);
			sourceFilePath.chmod(0755);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void complete(AgentMeta agentMeta) {
		CommonFunction.deployComplete(agentMeta, filePathHelper);
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
