package com.agun.flyJenkins.model;

public class DeployReport {
	
	private String deployId;
	private int deploySize = 0;
	private int successCount =0;
	private int failCount = 0;
	
	public String getDeployId() {
		return deployId;
	}
	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}
	public int getDeploySize() {
		return deploySize;
	}
	public void setDeploySize(int deploySize) {
		this.deploySize = deploySize;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	
	public void plusSuccessCount(){
		this.successCount++;
	}
	
	public void plusFailCount(){
		this.failCount++;
	}
	
	public boolean isFinished(){
		if(deploySize == 0)
			return true;
		if(deploySize == (failCount + successCount))
			return true;
		
		return false;
	}
	
	public int getNextOrder(){
		if(isFinished()){
			return 0;
		}
		
		if(deploySize > (failCount + successCount))
			return failCount + successCount +1;
		
		return 0;
	}

}
