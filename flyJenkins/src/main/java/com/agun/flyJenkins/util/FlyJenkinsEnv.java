package com.agun.flyJenkins.util;

import java.io.File;

import jenkins.model.Jenkins;

public class FlyJenkinsEnv {

	public static String getProductionRoot(){
		return Jenkins.getInstance().getRootDir().getAbsolutePath() + "/flyJenkins/job";
	}
}
