package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.deploy.DeployRequest;
import com.agun.flyJenkins.ui.ConfigServiceMeta.DescriptorImpl;

import hudson.Extension;

@Extension
public class DeployInfo extends FlyUI {

	private List<DeployRequest> deployRequestList;
	
	public DeployInfo(){
		System.out.println("====> jobName");
	}
	
	@Override
	public String getDescription() {
		return "you can see request deploy info";
	}

	
	/**
     * request 한 데이터를 저장한다.
     * @param request
     * @param response
     */
    public void doSave(final StaplerRequest request, final 
    		StaplerResponse response) { 
    
    	String jobName  = request.getParameter("jobName");
    	
    	System.out.println("=====> save");
    }
    
    public List<DeployRequest> getDeployRequestList(){
    	List<DeployRequest> deployRequestList = new ArrayList<DeployRequest>();
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.setDate(new Date());
    	deployRequest.setJobName("testJob");
    	deployRequest.setLicenser("song");
    	deployRequest.setProduction("start.jar");
    	deployRequest.setRequester("agun");
    	deployRequestList.add(deployRequest);
    	return deployRequestList;
    	/*
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.load();
    	return deployRequest.getDeployRequestList();*/
    }
    
	
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
    
	@Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
	
		private String jobName;
		
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
