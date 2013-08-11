package com.agun.jenkins;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.agun.flyJenkins.process.FlyProcess;

import jenkins.model.Jenkins;

import hudson.model.Action;
import hudson.remoting.Callable;

public class CLICallable implements Callable<Map<String, Object>, Throwable> {

	private String operationName;
	private String processName;
	private Integer agentId;
	
	public Integer getAgentId() {
		return agentId;
	}


	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}


	public String getProcessName() {
		return processName;
	}

	public String getOperationName() {
		return operationName;
	}


	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}


	@Override
	public  Map<String, Object> call() throws Throwable {
		
		List<Action> actionList = Jenkins.getInstance().getActions();
		
		for(Action action : actionList){
			
			if(action.getDisplayName() != null && action.getDisplayName().equals("flyJenkins")){
				try {
					Method method = action.getClass().getMethod("getFlyProcess", String.class);
					FlyProcess flyProcess =  (FlyProcess) method.invoke(action, processName);
					return flyProcess.run(agentId, operationName);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
