package org.flyJenkins.rest.model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Production{

	private int productionID;
	
	// job id of building
	private int jobID;
	private String jobName;
	private int buildNumber;
	
	// 
	private String output;
	// building time
	private Date createDate;
	
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public int getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

	public String getCreateDate() {
		if(this.createDate ==  null)
			return null;
		
		DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		return sdFormat.format(this.createDate);
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public int getProductionID() {
		return productionID;
	}

	public void setProductionID(int productionID) {
		this.productionID = productionID;
	}
}
