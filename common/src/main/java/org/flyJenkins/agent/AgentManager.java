/**
 * define management agent 
 * @author agun
 */

package org.flyJenkins.agent;

import java.util.List;

import org.flyJenkins.model.Agent;
import org.flyJenkins.model.ServiceMeta;

public interface AgentManager {
	
	/**
	 * register agnet when start agent
	 * @param Agent  
	 * @return List<ServiceMeta>
	 */
	public List<ServiceMeta> register(Agent agent);
	
	/**
	 * complete starting of agent
	 * @param Agent
	 * @return boolean
	 */
	public boolean complete(Agent agent);
}
