package com.agun.agent.adapter;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;

public class AdapterFactory {

	private AdapterFactory(){}
	private static TomcatService tomcatService = new TomcatService();
	private static GeneralService generalService = new GeneralService();
	
	public static ServiceType getServiceType(AgentMeta agentMeta, FilePathHelper filePathHelper){
		if(agentMeta.getType() == 2){
			tomcatService.setFilePathHelper(filePathHelper);
			return tomcatService;
		}
		
		return generalService;
	}
}
