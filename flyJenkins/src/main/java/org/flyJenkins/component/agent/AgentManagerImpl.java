package org.flyJenkins.component.agent;

import java.util.List;

import org.flyJenkins.agent.AgentManager;
import org.flyJenkins.model.Agent;
import org.flyJenkins.model.ServiceMeta;

public class AgentManagerImpl implements AgentManager {

	public boolean complete(Agent agent) {
		return false;
	}

	public List<ServiceMeta> register(Agent agent) {
		return null;
	}

}
