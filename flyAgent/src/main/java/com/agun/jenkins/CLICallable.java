package com.agun.jenkins;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import jenkins.model.Jenkins;

import hudson.model.Action;
import hudson.remoting.Callable;

public class CLICallable implements Callable<Object, Throwable> {

	private Object[] argList;
	private String methodName;
	private String displayName;
	
	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgList() {
		return argList;
	}


	public void setArgList(Object[] argList) {
		this.argList = argList;
	}


	@Override
	public Object call() throws Throwable {
	
		
		List<Action> actionList = Jenkins.getInstance().getActions();
		
		Class<?>[] classList =  null;
		
		if(argList.length > 0)
			classList = new Class<?>[argList.length];
		
		int i =0;
		for(Object arg : argList){
			classList[i] = arg.getClass();
			i++;
		}
		
		for(Action action : actionList){
			if(action.getDisplayName() != null && action.getDisplayName().equals(displayName)){
				try {
					Method method = action.getClass().getMethod(methodName, classList);
					return method.invoke(action, argList);
					
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
