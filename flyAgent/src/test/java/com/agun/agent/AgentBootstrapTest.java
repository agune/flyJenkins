package com.agun.agent;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.agun.agent.model.AgentMemoryStore;
import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.CLIHelper;

public class AgentBootstrapTest {

	@Test
	public void startTest() {
		URL url  = this.getClass().getResource("/id_rsa");
		AgentBootstrap agentBootstrap = new AgentBootstrap();
		CLIHelper cliHelper = agentBootstrap.start(url.getPath(), "http://127.0.0.1:8080/jenkins");
		cliHelper.destory();
		
		AgentMemoryStore agentMemoryStrore = AgentMemoryStore.getInstance();
		
		List<AgentMeta> agentList = agentMemoryStrore.getAgentMetaList();
		
		for(AgentMeta agentMeta : agentList){
			System.out.println("========> ");
			System.out.println(agentMeta.getId());
			System.out.println(agentMeta.getDestination());
			System.out.println(agentMeta.getServerId());
			System.out.println(agentMeta.getType());
			System.out.println(agentMeta.getTestCmd());
			System.out.println("pid: " + agentMeta.getPid());
			System.out.println("========> end");
					
		}
	}

}
