package com.agun.flyJenkins;

import com.agun.flyJenkins.service.NetworkSpace;

public class FlyMemStore {
	private NetworkSpace networkSpace;

	public FlyMemStore(){
	}
	public NetworkSpace getNetworkSpace() {
		return networkSpace;
	}

	public void setNetworkSpace(NetworkSpace networkSpace) {
		this.networkSpace = networkSpace;
	}
}
