package com.agun.agent.adapter;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import hudson.FilePath;
import hudson.Launcher.LocalLauncher;
import hudson.util.StreamTaskListener;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;
import com.agun.system.SystemUtil;

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
		agentMeta.setPid(pid);
		return pid;
	}

	@Override
	public boolean deploy(AgentMeta agentMeta) {
		stop(agentMeta);
		return start(agentMeta);
	}
	
	@Override
	public boolean getProduction(AgentMeta agentMeta, String production) {
		String filename = extractFilename(production);
		AgentInfoManager.checkProductionDir(agentMeta.getServiceId());
		
		String productionPath = AgentInfoManager.getProductionPath(agentMeta.getServiceId(), filename);
	
		FilePath filePath = new FilePath(new File(productionPath));
		
		try {
			filePathHelper.copyTo(production, productionPath);
			if(filePath.exists()){
				System.out.println("======>" + agentMeta.getDestination() + "/webapps/" + filename + "," + productionPath);
				FilePath sourceFilePath = new FilePath(new File(agentMeta.getDestination() + "/webapps/" + filename));
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
	private String extractFilename(String filepath){
		String filePathNormal = filepath.replaceAll("\\\\", "/");
		String[] fileExtractList = filePathNormal.split("/");
		if(fileExtractList != null && fileExtractList.length > 0){
			return fileExtractList[fileExtractList.length -1];
		}
		return filepath;
	}
	@Override
	public boolean stop(AgentMeta agentMeta)  {
		int pid = getPid(agentMeta);
		if(pid > 0){
			String shutdownPath = shutDownScriptPath(agentMeta);
			 LocalLauncher launcher = new LocalLauncher(new StreamTaskListener(System.out, null));
			 Map<String, String> envTable = new Hashtable<String, String>();
			 envTable.put("CATALINA_HOME", agentMeta.getDestination());
			 try {
				launcher.launch().cmds(shutdownPath)
				.envs(envTable)
				.stderr(System.out)
				.start().join();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			 return checkPid(true, agentMeta);
		}
		return false;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		int pid = getPid(agentMeta);
		if(pid == 0){
			String startPath = startScriptPath(agentMeta);
			 LocalLauncher launcher = new LocalLauncher(new StreamTaskListener(System.out, null));
			 Map<String, String> envTable = new Hashtable<String, String>();
			 envTable.put("CATALINA_HOME", agentMeta.getDestination());
			 try {
				launcher.launch().cmds(startPath)
				.envs(envTable)
				.start();
				return checkPid(false, agentMeta);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return false;
	}
	
	@Override
	public boolean monitoring(AgentMeta agentMeta){
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
	
	/**
	 * 서버 구동 스크립트를 경로를 구한다.
	 * @param agentMeta
	 * @return
	 */
	private String shutDownScriptPath(AgentMeta agentMeta){
		if(agentMeta.getDestination() == null)
			return null;
		if(SystemUtil.isWindows())
			return agentMeta.getDestination() + "/bin/shutdown.bat";
		return agentMeta.getDestination() + "/bin/shutdown.sh";
	}
	
	/**
	 * 서버 중지 스크립트를 찾는다. 
	 * @param agentMeta
	 * @return
	 */
	private String startScriptPath(AgentMeta agentMeta){
		if(agentMeta.getDestination() == null)
			return null;
		if(SystemUtil.isWindows())
			return agentMeta.getDestination() + "/bin/startup.bat";
		return agentMeta.getDestination() + "/bin/startup.sh";
	}

	@Override
	public void complete(AgentMeta agentMeta) {
		
	}
}
