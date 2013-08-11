package com.agun.flyJenkins;

import com.agun.flyJenkins.service.NetworkSpace;

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
	}
	
	/**
	 * networkSpace 를 초기화 한다. 
	 */
	public static void initNetworkSpace(){
		NetworkSpace networkSpace = NetworkSpace.getInstance();
		networkSpace.initNetworkSpace();
	}
	
}
