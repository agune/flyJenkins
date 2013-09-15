package com.agun.flyJenkins.util;


import jenkins.model.Jenkins;

public class FlyJenkinsEnv {

	public static String getProductionRoot(){
		return Jenkins.getInstance().getRootDir().getAbsolutePath() + "/flyJenkins/job";
	}
	
	public static String getLastBuildPath(int serviceId){
		return Jenkins.getInstance().getRootDir().getAbsolutePath() + "/flyJenkins/lastBuild/"+serviceId+"/service.zip";
	}
}
