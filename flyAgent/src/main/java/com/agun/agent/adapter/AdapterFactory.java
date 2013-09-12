package com.agun.agent.adapter;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.FilePathHelper;

public class AdapterFactory {

	private AdapterFactory(){}
	private static TomcatService tomcatService = new TomcatService();
	private static GeneralService generalService = new GeneralService();
	private static EtcService etcService = new EtcService();
	public static ServiceType getServiceType(AgentMeta agentMeta, FilePathHelper filePathHelper){
		if(agentMeta.getType() == 1){
			generalService.setFilePathHelper(filePathHelper);
			return generalService;
		}else if(agentMeta.getType() == 2){
			tomcatService.setFilePathHelper(filePathHelper);
			return tomcatService;
		}
		etcService.setFilePathHelper(filePathHelper);
		return etcService;
	}
}
