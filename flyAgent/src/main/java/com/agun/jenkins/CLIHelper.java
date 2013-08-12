/**
 * jenkins  의 CLI 기능을 추상화 하여 구현
 * @author agun
 */
package com.agun.jenkins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Collections;
import java.util.Map;

import hudson.cli.CLI;
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
	
	public Map<String, Object> callActionFunction(final String processName, final String operationName,  Object arg1){
		try {
			CLICallable callable = new CLICallable();
			callable.setArg1(arg1);
			callable.setOperationName(operationName);
			callable.setProcessName(processName);
			return channel.call(callable);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Channel getChannel(){
		return this.channel;
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
