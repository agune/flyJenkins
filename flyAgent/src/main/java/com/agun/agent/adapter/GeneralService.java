package com.agun.agent.adapter;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.ProcessTreeHelper;



public class GeneralService implements ServiceType {

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getProduction(AgentMeta agentMeta, String production) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stop(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(AgentMeta agentMeta) {
		// TODO Auto-generated method stub
		return false;
	}
}
