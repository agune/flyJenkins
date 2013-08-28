package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.deploy.DeployRequest;
import com.agun.flyJenkins.job.JobExtends;
import com.agun.flyJenkins.ui.ConfigServiceMeta.DescriptorImpl;
import com.agun.flyJenkins.util.AjaxProxy;

import hudson.Extension;
import hudson.model.User;

@Extension
public class DeployInfo extends FlyUI {

	public DeployInfo(){
	
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
    	Map<String, Object> resultMap = FlyFactory.getPropertiesOfJob(jobName);
    	//String buildPath = FlyFactory.getRootPathOfJob(jobName);
    	if(resultMap.size() == 0)
    		return;
    	String production = "";
    	String licenser = "";
    	int serverGroup = 0;
    	for(Entry entry : resultMap.entrySet()){
    		if(entry.getValue() instanceof JobExtends){
    			JobExtends jobExtend = (JobExtends) entry.getValue();
    			production =  jobExtend.production;
    			licenser  = jobExtend.licenser;
    			serverGroup =  jobExtend.serverGroup;
    			break;
    		}
    	}
    	User user = User.current();
     	if(user == null)
    		return;
   
    	String usedId = user.getId();
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.setDate(new Date());
    	deployRequest.setJobName(jobName);
    	deployRequest.setLicenser(licenser);
    	deployRequest.setProduction(production);
    	deployRequest.setRequester(usedId);
    	deployRequest.setServerGroup(serverGroup);
    	
    	try {
			deployRequest.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<DeployRequest> getDeployRequestList(String jobName){
    	if(jobName == null)
    		return Collections.EMPTY_LIST;
    	
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.load();
    	List<DeployRequest> saveDeployRequestList =deployRequest.getDeployRequestList();
    	if(saveDeployRequestList == null)
    		return Collections.EMPTY_LIST;
    	List<DeployRequest> deployRequestList = new ArrayList<DeployRequest>();
    	for(DeployRequest request : saveDeployRequestList){
    		if(request.getJobName().equals(jobName))
    			deployRequestList.add(request);
    	}
    	return deployRequestList;
    }
    
    public void setComplete(String jobName, Date date){
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.load();
    	
    	List<DeployRequest> requestList = deployRequest.getDeployRequestList();
    	System.out.println("====> " + jobName + "," + date);
    	DeployRequest saveDeployRequest = null;
    	for(DeployRequest iDeployRequest : requestList){
    		if(iDeployRequest.getDate().getTime() == date.getTime() 
    				&& iDeployRequest.getJobName().equals(jobName)){ 
    			saveDeployRequest = iDeployRequest;
    			saveDeployRequest.setRun(true);
    			break;
    		}
    	}
    
    	if(saveDeployRequest == null)
    		return;
    	
    	System.out.println("====> " + saveDeployRequest.getDate());
    	try {
    		saveDeployRequest.setDeployRequestList(requestList);
			saveDeployRequest.edit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public AjaxProxy getAjaxProxy(){
 		return AjaxProxy.getInstance();
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
