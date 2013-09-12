package com.agun.flyJenkins;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;

import jenkins.model.Jenkins;


/**
 * flyJenkins 의 초기화를 도와 주는 class
 * @author agun
 *
 */
public class FlyBootstrap {
	
	/**
	 * FlyJenkins 를 시작한다.
	 */
	public static void start(){
		FlyBootstrap.initNetworkSpace();
		
		File fileRootPath = Jenkins.getInstance().getRootDir();
		String flyJenkinsRoot = fileRootPath.getAbsolutePath() + "/flyJenkins/job";
		FilePath filePath =  new FilePath(new File(flyJenkinsRoot));
		
		try {
			if(filePath.exists() == false){
				filePath.mkdirs();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * networkSpace 를 초기화 한다. 
	 */
	public static void initNetworkSpace(){
		FlyFactory.getNetworkSpace().initNetwork();
	}
	
}
