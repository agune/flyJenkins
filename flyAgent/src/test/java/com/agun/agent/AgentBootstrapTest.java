package com.agun.agent;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.CLIHelper;

public class AgentBootstrapTest {
	
	@Ignore
	@Test
	public void startTest() {
		URL url  = this.getClass().getResource("/id_rsa");
		String agentHost = null;
		
		try {
			agentHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AgentBootstrap agentBootstrap = new AgentBootstrap(agentHost);
		CLIHelper cliHelper = agentBootstrap.start(url.getPath(), "http://127.0.0.1:8080/jenkins");
		cliHelper.destory();
		
		AgentMemoryStore agentMemoryStrore = AgentMemoryStore.getInstance();
		
		List<AgentMeta> agentList = agentMemoryStrore.getAgentMetaList();
		
		for(AgentMeta agentMeta : agentList){
			System.out.println("========> ");
			System.out.println(agentMeta.getId());
			System.out.println(agentMeta.getDestination());
			System.out.println(agentMeta.getServiceId());
			System.out.println(agentMeta.getType());
			System.out.println(agentMeta.getCommand());
			System.out.println("pid: " + agentMeta.getPid());
			System.out.println("========> end");
					
		}
	}
	
	@Ignore
	@Test
	public void initTest(){
		URL url  = this.getClass().getResource("/id_rsa");
		
		String agentHost = null;
		
		try {
			agentHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AgentBootstrap agentBootstrap = new AgentBootstrap(agentHost);
		
		CLIHelper cliHelper= agentBootstrap.auth(url.getPath(), "http://127.0.0.1:8080/jenkins");
		agentBootstrap.init(cliHelper);
		cliHelper.destory();
		
	}

}
