/**
 * flyJenkins의 flyAgent를 구현함
 * @author agun
 */
package com.agun.agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.agent.process.CheckRequest;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.FilePathHelper;

public class flyAgent implements Daemon{

	private Thread thread;
	private boolean stoped= false;
	private boolean lastOneWasTick = false;
	private String agentHost = null;
	
	@Override
	public void destroy() {
		thread = null;
	}

	@Override
	public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {
		String[] args =  daemonContext.getArguments();
		
		doStart(args);
	}

	public void doStart(String[] args){

		final String host = args[0];
		final String rasDir = args[1];
		
		thread = new Thread(){
			private long lastTick =  0;
			@Override
			public synchronized void start(){
				
				AgentBootstrap agentBootstrap =null;
				
				if(agentHost == null){
					try {
						agentHost = InetAddress.getLocalHost().getHostName();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				agentBootstrap = new AgentBootstrap(agentHost);
				CLIHelper cliHelper = agentBootstrap.start(rasDir, "http://"+ host);
				FilePathHelper filePathHelper = new FilePathHelper(cliHelper);
				AgentMemoryStore agentMemory = AgentMemoryStore.getInstance();
				CheckRequest checkRequest = new CheckRequest(cliHelper, filePathHelper, agentHost);
				
				while(!stoped){
					long now = System.currentTimeMillis();
					if(now - lastTick >= 1000){
						if(lastOneWasTick){
							List<AgentMeta> agentMetaList = agentMemory.getAgentMetaList();
							checkRequest.process(agentMetaList);
							checkRequest.checkDeploy();
						}
						lastOneWasTick = !lastOneWasTick;
						lastTick = now;
					}
				}
				cliHelper.destory();
			}
		};
	}
	@Override
	public void start() throws Exception {
		thread.start();
	}

	public void stop() throws Exception {
			stoped = true;
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				throw e;
			}
	}
	
	public void setAgentHost(String host){
		this.agentHost = host;
	}
	
}
