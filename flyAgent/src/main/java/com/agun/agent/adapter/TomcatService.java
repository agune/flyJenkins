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
import org.apache.log4j.Logger;

import hudson.FilePath;
import hudson.Launcher.LocalLauncher;
import hudson.util.StreamTaskListener;

import com.agun.agent.function.CommonFunction;
import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.flyJenkins.util.FlyJenkinsInfoManager;
import com.agun.jenkins.FilePathHelper;
import com.agun.jenkins.ProcessTreeHelper;
import com.agun.system.AgentInfoManager;
import com.agun.system.SystemUtil;

public class TomcatService implements ServiceType {

	static Logger log = Logger.getLogger(TomcatService.class.getName());
	
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
		
		/**
		 *  obtain filename of production
		 */
		String filename = extractFilename(production);
		
		/**
		 * make dir of production on agent
		 */
		AgentInfoManager.checkProductionDir(agentMeta.getServiceId());
		
		/**
		 * obtain path of production on agent
		 */
		String productionPath = AgentInfoManager.getProductionPath(agentMeta.getServiceId(), filename);
	
		
		try {
			FilePath filePath = new FilePath(new File(productionPath));
			/**
			 *production of agent copy to  destination
			 */
			filePathHelper.copyTo(production, productionPath);
			
			/**
			 * production move agent destination => service destination
			 */
			if(filePath.exists()){
				
				
				
				FilePath sourceFilePath = new FilePath(new File(agentMeta.getDestination() + "/webapps/" + filename));
				filePath.renameTo(sourceFilePath);
				
				log.debug("deployment copy completed : " +  productionPath + " to " + agentMeta.getDestination() + "/webapps/" + filename );
				
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
			 
			 log.debug(" stop tomcat : " + shutdownPath);
			 
			 try {
				launcher.launch().cmds(shutdownPath)
				.envs(envTable)
				.stderr(System.out)
				.start();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			 return checkPid(true, agentMeta);
		}
		return false;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		int pid = getPid(agentMeta);
		boolean isOk = false;
		if(pid == 0){
			String startPath = startScriptPath(agentMeta);
			 LocalLauncher launcher = new LocalLauncher(new StreamTaskListener(System.out, null));
			 Map<String, String> envTable = new Hashtable<String, String>();
			 envTable.put("CATALINA_HOME", agentMeta.getDestination());
			 try {
				launcher.launch().cmds(startPath)
				.envs(envTable)
				.start();
				log.debug(" start tomcat : " + startPath);
				isOk = checkPid(false, agentMeta);
				if(	isOk 
						&& (agentMeta.getCommand() !=null) 
						&& (agentMeta.getCommand().length() > 0)){
					isOk = runCommand(agentMeta.getDestination() + "/bin/fly_command.sh");
				}
			 } catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return isOk;
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
				
				log.debug(agentMeta.getTestUrl() + " testing " + " status code : " + statusCode);
				
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
	
	
	private boolean runCommand(String command){
		LocalLauncher launcher = new LocalLauncher(new StreamTaskListener(System.out, null));
        try {
                launcher.launch().cmds(command).stderr(System.out)
                .stdout(System.out)
                .start();
                
                log.debug("run command : " + command);
        } catch (IOException e) {
                e.printStackTrace();
                return false;
        }         
        return true;
	}
	
	private void createCommandSh(String destination, String command){
		if(command == null || command.length() == 0)
			return;
		createSh(destination, "bin/fly_command.sh", command);
	}
	
	private void createSh(String destination, String scriptName, String command){
		FilePath sourceFilePath = new FilePath(new File(destination + "/" + scriptName));
		try {
			sourceFilePath.write(command, null);
			sourceFilePath.chmod(0755);
			log.debug(" make script : " + destination + "/" + scriptName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
					log.debug(" check pid : " + pid);
					
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
		CommonFunction.deployComplete(agentMeta, filePathHelper);
	}
}
