/**
 * jenkins  의 CLI 기능을 추상화 하여 구현
 * @author agun
 */
package com.agun.jenkins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;

import jenkins.model.Jenkins;

import hudson.cli.CLI;
import hudson.model.Action;
import hudson.remoting.Callable;
import hudson.remoting.Channel;

public class CLIHelper {

	private CLI cli = null;
	private Channel channel = null;
	
	public CLIHelper(String url, String rsaKeyPath) {
		this.realCreateCLI(url, new File(rsaKeyPath));
	}
	
	public CLIHelper(String url, File file) {
		this.realCreateCLI(url, file);
	}
	
	public Object callActionFunction(final String displayName, final String methodName, final Object... argList){
		try {
				channel.call(new Callable<Object, Exception>() {

					@Override
					public Object call() throws Exception {
						// TODO Auto-generated method stub
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
				
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	/**
	 * 실제로 cli 를 인증하고 인스턴스를 구한다.
	 * @param url      
	 * @param rsaFile
	 */
	private void realCreateCLI(String url, File rsaFile){
		try {
			cli = new CLI(new URL(url));
			KeyPair key = cli.loadKey(rsaFile);
			cli.authenticate(Collections.singleton(key));
			cli.upgrade();
			channel = cli.getChannel();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	public void destory(){
		if(cli != null){
			try {
				cli.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
