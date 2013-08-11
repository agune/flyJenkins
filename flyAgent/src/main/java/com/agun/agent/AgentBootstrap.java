package com.agun.agent;

import com.agun.jenkins.CLIHelper;

public class AgentBootstrap {
	
	public CLIHelper auth(String rsaPath, String jenkinsHost){
		CLIHelper cliHelper = new CLIHelper(jenkinsHost, rsaPath);
		return cliHelper;
	}
	
	public void init(CLIHelper cliHelper){
		//cliHelper.callActionFunction(displayName, methodName, argList);
	}
	
	public void identity(){
		
	}
	
	public void complete(){
		
	}
}
