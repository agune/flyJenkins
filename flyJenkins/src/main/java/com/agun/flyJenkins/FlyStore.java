package com.agun.flyJenkins;

import com.agun.flyJenkins.service.ServerMeta;

public class FlyStore {
	public static ServerMeta getServerMeta(){
		ServerMeta serverMeta = new ServerMeta();
		serverMeta.load();
		return serverMeta;
	}
}
