package com.agun.agent.adapter;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

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
}
