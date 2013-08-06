/**
 *  서비스의 타입을 나타내는 인터페이스
 *  @author agun
 *  
 */
package com.agun.agent.adapter;

import com.agun.agent.model.AgentMeta;

public interface ServiceType {

	public int getPid(AgentMeta agentMeta);
	
	public boolean deploy(AgentMeta agentMeta);
}
