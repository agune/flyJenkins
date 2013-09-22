package com.agun.agent.adapter;

import hudson.FilePath;
import hudson.Launcher.LocalLauncher;
import hudson.util.StreamTaskListener;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.agun.agent.function.CommonFunction;
import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;

public class EtcService implements ServiceType {
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
				FilePath sourceFilePath = new FilePath(new File(agentMeta.getDestination() + "/" + filename));
				filePath.renameTo(sourceFilePath);
				
				sourceFilePath.chmod(0755);
				
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
	public boolean deploy(AgentMeta agentMeta) {
		stop(agentMeta);
		return start(agentMeta);
	}

	@Override
	public boolean stop(AgentMeta agentMeta) {
		return true;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		return runCommand(agentMeta.getDestination() + "/command.sh");
	}

	@Override
	public boolean monitoring(AgentMeta agentMeta) {
		if(agentMeta.getTestUrl() != null && agentMeta.getTestUrl().length() > 0){
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(agentMeta.getTestUrl());
			
			try {
				HttpResponse response1 =  httpClient.execute(httpGet);
				StatusLine statusLine = response1.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if(statusCode == 200)
					return true;
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public void complete(AgentMeta agentMeta) {
		CommonFunction.deployComplete(agentMeta, filePathHelper);
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
	
	private String extractFilename(String filepath){
		String filePathNormal = filepath.replaceAll("\\\\", "/");
		String[] fileExtractList = filePathNormal.split("/");
		if(fileExtractList != null && fileExtractList.length > 0){
			return fileExtractList[fileExtractList.length -1];
		}
		return filepath;
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
	
	
	public FilePathHelper getFilePathHelper() {
		return filePathHelper;
	}

	public void setFilePathHelper(FilePathHelper filePathHelper) {
		this.filePathHelper = filePathHelper;
	}
}
