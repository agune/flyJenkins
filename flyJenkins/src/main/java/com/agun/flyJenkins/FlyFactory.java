package com.agun.flyJenkins;

public class FlyFactory {

	private FlyFactory(){
		
	}
	public static FlyDeploy getFlyDeploy(){
		return FlyDeploy.getInstance();
		
	}
	
}
