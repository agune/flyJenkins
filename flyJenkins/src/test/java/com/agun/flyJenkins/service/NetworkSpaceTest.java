package com.agun.flyJenkins.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NetworkSpaceTest {

	@Test
	public void attachAgentTest() {
		NetworkSpace networkSpace = new NetworkSpace();
		
		AgentService agentService = createMojoAgentService("127.0.0.1", 1, 1);
		networkSpace.attachAgent(agentService);
		
		agentService = createMojoAgentService("127.0.0.1", 2, 1);
		networkSpace.attachAgent(agentService);

	
	
		Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
		assertTrue("network map 구성이 올바르지 않습니다,", networkMap.size() == 1);
		
		agentService = createMojoAgentService("127.0.0.2", 2, 1);
		networkSpace.attachAgent(agentService);
		assertTrue("network map 에 agent 가 추가 되지 않았습니다.,", networkMap.size() == 2);
	
		agentService = createMojoAgentService("127.0.0.1",3, 1);
		networkSpace.attachAgent(agentService);
	
		agentService = createMojoAgentService("127.0.0.1",2, 2);
		networkSpace.attachAgent(agentService);
		
		assertTrue("agent 구성이 올바르지 않습니다.", networkMap.get("127.0.0.1").size() == 3);

		
	}
	
	private ServiceGroup createMojoServiceGroup(int groupId){
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupId(groupId);
		return serviceGroup;
	}
	
	private AgentService createMojoAgentService(String host, int agentId, int serviceGroupId){
		AgentService agentService = new AgentService();
		agentService.setAgentId(agentId);
		agentService.setHost(host);
		ServiceGroup serviceGroup = createMojoServiceGroup(serviceGroupId);
		agentService.setServiceGroup(serviceGroup);
		return agentService;
	}

}
